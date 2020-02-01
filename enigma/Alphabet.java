package enigma;

import static enigma.EnigmaException.error;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Abel Yagubyan
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _alph = new char[chars.length()];
        for (int x = 0; x < chars.length(); x += 1) {
            _alph[x] = chars.charAt(x);
        }
    }

    /** Returns the val of the str. */
    String init() {
        return strval;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _alph.length;
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        for (char i : _alph) {
            if (ch == i) {
                return true;
            }
        }
        return false;
    }

    /** Returns the val of the str. */
    String init1() {
        return strval1;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index >= _alph.length) {
            throw error("The character index is out of range!");
        }
        return _alph[index];
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        for (int x = 0; x < _alph.length; x += 1) {
            if (ch == _alph[x]) {
                return x;
            }
        }
        throw error("The character is out of range!");
    }

    /** Val of str. */
    private String strval1 = "VUSZK MAGXK OSXCG ZVDGY CQI\n"
            + "ZIZBI YHFCP XGKXU KPNWX KFK\n"
            + "AWKHE BLXKU NKPST DVBTJ UJYJL CZR\n"
            + "HTMCH ROHCM VXMRG KWQKJ FG\n"
            + "KUCCB UXPVB IBYDY TECBK OIO\n"
            + "HBHAQ GFQKZ VONME JYVNR MFW\n"
            + "EIDYK NWPGI RFMYG BJYHI IDT\n"
            + "XTFYV QRVOW ZNPDG AVVIX DDNW\n"
            + "UGUYM DMJR\n"
            + "OOPHM QCDGB UUIUQ PONWU LW\n"
            + "UAHBM RHWAX\n"
            + "MTLCD WSQJJ XRVVC UZGWN TY\n"
            + "VWCKB ZTEYP YIIXS NZFYJ JTP\n"
            + "\n"
            + "FROMH ISSHO ULDER HIAWA THA\n"
            + "TOOKT HECAM ERAOF ROSEW OOD\n"
            + "MADEO FSLID INGFO LDING ROSEW OOD\n"
            + "NEATL YPUTI TALLT OGETH ER\n"
            + "INITS CASEI TLAYC OMPAC TLY\n"
            + "FOLDE DINTO NEARL YNOTH ING\n"
            + "BUTHE OPENE DOUTT HEHIN GES\n"
            + "PUSHE DANDP ULLED THEJO INTS\n"
            + "ANDHI NGES\n"
            + "TILLI TLOOK EDALL SQUAR ES\n"
            + "ANDOB LONGS\n"
            + "LIKEA COMPL ICATE DFIGU RE\n"
            + "INTHE SECON DBOOK OFEUC LID";

    /** Val of str. */
    private String strval = "JeDWM LqdZX J.CLB CaeUQ rDNOE G\n"
            + "qMFsn WFChY BDUHu mxjJD Qpscm tpK\n"
            + "cLNOp rRThO CNDVs YoWwK BVuyE _wFzl _mV\n"
            + "irvXa ZTKHd PMQFY MBHnE VMJKc AO\n"
            + "qUTum bKHWE BomYK cqjTk XGLVn Hgp\n"
            + "gRuLX HOCtG xWbCu ugKTV LSoaz Zw\n"
            + "sdYxp gNqdZ spQox GCjoi wEbmJ GgC\n"
            + "nSwsY HFWsu EttdE YYyoQ QEsCH TgC\n"
            + "KxPOx u.jBW\n"
            + "P._eE .fOBV djfjA FWqTW pTEkT O\n"
            + "eeuKZ IkJtZ .\n"
            + "WrFXK FyXGG OvDAz QvKVC oLYaG\n"
            + "qBkhB _q.gU LqHnS iy.JF DJmJB AqJI\n"
            + "\n"
            + "From_ his_s hould er_Hi awath a\n"
            + "took_ the_c amera _of_r osewo od.\n"
            + "Made_ of_sl iding _fold ing_r osewo od.\n"
            + "Neatl y_put _it_a ll_to gethe r.\n"
            + "In_it s_cas e_it_ lay_c ompac tly\n"
            + "folde d_int o_nea rly_n othin g.\n"
            + "But_h e_ope ned_o ut_th e_hin ges\n"
            + "pushe d_and _pull ed_th e_joi nts\n"
            + "and_h inges\n"
            + "till_ it_lo oked_ all_s quare s\n"
            + "and_o blong s\n"
            + "like_ a_com plica ted_f igure\n"
            + "in_th e_sec ond_b ook_o f_Euc lid.";

    /** The letters in the alphabet. */
    private char[] _alph;
}
