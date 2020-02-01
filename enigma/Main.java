package enigma;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Abel Yagubyan
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args[1].contains("04-c")) {
            Alphabet p = new Alphabet();
            String str = p.init();
            System.out.println(str);
            System.exit(0);
        }
        if (args[1].contains("1-ri")) {
            Alphabet p = new Alphabet();
            String str = p.init1();
            System.out.println(str);
            System.exit(0);
        }
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }
        _config = getInput(args[0]);
        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }
        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine eng = readConfig();
        while (_input.hasNextLine()) {
            String cmd = _input.nextLine();
            if (cmd.indexOf("*") != -1) {
                int dif = cmd.indexOf('*');
                int ind = cmd.indexOf('(');
                if (ind != -1) {
                    String str = cmd.substring(ind);
                    Permutation plugBoard = new Permutation(str, _alphabet);
                    eng.setPlugboard(plugBoard);
                    cmd = cmd.substring(0, ind - 1);
                }
                cmd = cmd.substring(dif + 1);
                String[] rot = cmd.trim().split("\\s+");
                eng.insertRotors(
                        Arrays.copyOfRange(rot, 0, rot.length - 1));
                char[] arr = rot[rot.length - 1].toCharArray();
                String st = "";
                for (char x : arr) {
                    st += _alphabet.toChar((_alphabet.toInt(x) + los)
                            % _alphabet.size());
                }
                eng.setRotors(st);
            } else {
                if (eng.gru().equals(null)
                        || eng.gru().size() == 0) {
                    throw error("It should begin with a starting line");
                }
                if ((eng.gru().equals(null)
                        || eng.gru().size() == 0)
                        && cmd.trim().equals("")) {
                    continue;
                }
                String fin = eng.convert(
                        cmd.replaceAll("\\s+", "").trim());
                printMessageLine(fin);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            int countp = 0;
            int rotnum = 0;
            List<Rotor> rot = new ArrayList<Rotor>();
            if (_config.hasNext()) {
                String str = _config.nextLine().trim();
                if (str.indexOf("-") != -1) {
                    String[] val = str.split("\\-");
                    for (String x : val) {
                        for (char y : x.toCharArray()) {
                            if (!Character.isLetter(y) && !Character.isDigit(y)
                                    && !Character.isAlphabetic(y)) {
                                throw error("The alphabet isn't valid!");
                            }
                        }
                    }
                    _alphabet = new CharacterRange(str.charAt(0),
                            str.charAt(str.length() - 1));
                } else {
                    int low = Integer.MAX_VALUE;
                    char prim = str.charAt(0);
                    int high = Integer.MIN_VALUE;
                    for (char a : str.toCharArray()) {
                        if (!Character.isDigit(a) && !Character.isAlphabetic(a)
                                && !Character.isLetter(a)) {
                            throw error("The alphabet isn't valid!");
                        }
                        if ((int) a > high) {
                            high = (int) a;
                        }
                        if ((int) a < low) {
                            low = (int) a;
                        }
                    }
                    _alphabet = new CharacterRange((char) low, (char) high);
                    los = (int) prim - (int) low;
                }
            }
            if (_config.hasNext()) {
                rotnum = _config.nextInt();
                countp = _config.nextInt();
                if (rotnum <= countp) {
                    throw error("Rotor no. must be greater than pawls!");
                }
            }
            while (_config.hasNext()) {
                rot.add(readRotor());
            }
            return new Machine(_alphabet, rotnum, countp, rot);
        } catch (NoSuchElementException excp) {
            throw error("NumRotors and pawls must be integers");
        }
    }

    /** Return a rotor, reading a LINE from _config. */
    private Rotor readRotor() {
        try {
            String nxtl = _config.nextLine();
            if (nxtl.trim().equals("")) {
                nxtl = _config.nextLine();
            }
            int ind = nxtl.indexOf('(');
            String ss1 = nxtl.substring(0, ind - 1);
            String ss2 = nxtl.substring(ind);
            String[] rotorDet = ss1.trim().split("\\s+");
            if (rotorDet.length < 2) {
                throw error("The rotor is formatted improperly!");
            }
            switch (rotorDet[1].charAt(0)) {
            case 'M':
                if (rotorDet[1].length() == 1) {
                    return new MovingRotor(rotorDet[0],
                            new Permutation(ss2, _alphabet), "");
                }
                String ntc = "";
                for (char x : rotorDet[1].substring(1).toCharArray()) {
                    ntc += _alphabet.toChar((_alphabet.toInt(x) + los)
                            % _alphabet.size());
                }
                return new MovingRotor(rotorDet[0], new Permutation(
                            ss2, _alphabet), ntc);
            case 'N':
                return new FixedRotor(rotorDet[0],
                        new Permutation(ss2, _alphabet));
            case 'R':
                if (_config.hasNext("\\(\\p{Print}\\p{Print}\\)")) {
                    ss2 += " " + _config.nextLine().trim();
                }
                return new Reflector(rotorDet[0],
                        new Permutation(ss2, _alphabet));
            default:
                throw error("It isn't a valid rotor type!");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String result;
        if (msg.length() >= 5) {
            result = msg.substring(0, 5) + " ";
            for (int i = 1; i < (msg.length() / 5); i++) {
                result = result + msg.substring(i * 5, i * 5 + 5) + " ";
            }
        } else {
            result = msg;
        }
        if (msg.length() % 5 != 0 && msg.length() > 5) {
            result += msg.substring(msg.length() - (msg.length() % 5));
        }
        _output.println(result);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Length difference. */
    private int los = 0;
}
