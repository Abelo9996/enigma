package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Abel Yagubyan
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        this._n = notches.toUpperCase();
    }
    @Override
    boolean atNotch() {
        son(_n.indexOf(alphabet().toChar(
                permutation().wrap(setting()))) != -1 && rotates());
        return ion();
    }
    @Override
    boolean rotates() {
        return true;
    }
    @Override
    void advance() {
        sadv(rotates());
        set(setting() + 1);
    }
    /** Notches. */
    private String _n;
}

