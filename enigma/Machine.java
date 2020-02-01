package enigma;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Abel Yagubyan
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        this._rotnum = numRotors;
        _alphabet = alpha;
        _rotf = new HashMap<>();
        this._pawlcount = pawls;
        rotcoll = new ArrayList<>();
        for (Rotor x : allRotors) {
            _rotf.put(x.name().toUpperCase(), x);
        }
        _pboard = new Permutation("", _alphabet);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _rotnum;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawlcount;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length > 0) {
            int val = 0;
            rotcoll.clear();
            for (String x : rotors) {
                x = x.toUpperCase();
                if (rotcoll.contains(x)) {
                    throw error("Rotors can't be used more than once!");
                }
                if (!this._rotf.containsKey(x)) {
                    throw error("The rotor doesn't exist!");
                }
                if (this._rotf.get(x).rotates()) {
                    val += 1;
                }
                rotcoll.add(this._rotf.get(x));
            }
            if (numPawls() != val) {
                throw error("Pawls don't match the inserted amount!");
            }
            if (!rotcoll.get(0).reflecting()) {
                throw error("The first rotor has to be a reflector!");
            }
            if (_rotnum != rotcoll.size()) {
                throw error("_rotnum doesn't match inserted no. of rotors!");
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        char[] strchar = setting.toCharArray();
        if (setting.length() != numRotors() - 1) {
            throw error("The setting's length is incorrect!");
        }
        for (int x = 1; x < _rotnum; x += 1) {
            if (!_alphabet.contains(strchar[x - 1])) {
                throw error("The setting contains an invalid value!");
            }
            rotcoll.get(x).set(strchar[x - 1]);
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        this._pboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        c = _pboard.permute(c);
        for (int x = rotcoll.size() - 1; x >= 0; x -= 1) {
            rotcoll.get(x).atNotch();
        }
        if (rotcoll.get(rotcoll.size() - 1).rotates()) {
            rotcoll.get(rotcoll.size() - 1).advance();
        }
        for (int x = rotcoll.size() - 1; x >= 0; x -= 1) {
            if (x - 1 >= numRotors() - numPawls()
                    && rotcoll.get(x).adv()
                    && rotcoll.get(x - 1).rotates()
                    && rotcoll.get(x).ion()) {
                rotcoll.get(x - 1).advance();
            } else if (x - 1 >= numRotors() - numPawls()
                    && rotcoll.get(x - 1).rotates()
                    && rotcoll.get(x).ion()) {
                rotcoll.get(x - 1).advance();
                rotcoll.get(x).advance();
            }
            c = rotcoll.get(x).convertForward(c);
        }
        for (int x = 1; x < rotcoll.size(); x += 1) {
            rotcoll.get(x).sadv(false);
            c = rotcoll.get(x).convertBackward(c);
        }
        c = _pboard.invert(c);
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String str = "";
        for (char ch : msg.toCharArray()) {
            if (!_alphabet.contains(ch)) {
                throw error("The value isn't part of the alphabet!");
            }
            str += _alphabet.toChar(convert(_alphabet.toInt(ch)));
        }
        return str;
    }

    /** @return Gets the list of rotors used. */
    List<Rotor> gru() {
        return rotcoll;
    }

    /** @return Function used in UnitTest, complete setting of machine. */
    String getSetting() {
        String str = "";
        for (Rotor rot : rotcoll) {
            str += _alphabet.toChar(rot.setting());
        }
        return str;
    }


    /** Number of rotors.*/
    private int _rotnum;

    /** Maps the name to a rotor.*/
    private Map<String, Rotor> _rotf;

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of pawls on machine.*/
    private int _pawlcount;

    /** Machine's plugboard. */
    private Permutation _pboard;

    /** Used Rotor Collection.*/
    private List<Rotor> rotcoll;


}
