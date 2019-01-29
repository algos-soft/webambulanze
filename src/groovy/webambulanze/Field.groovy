package webambulanze

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 9-7-13
 * Time: 14:53
 */
public enum Field {

    txt(false, false, false),
    txtLink(false, false, true),
    txtObbEdit(true, true, false),
    txtEdit(false, true, false),
    numObbEdit(true, true, false),
    numEdit(false, true, false),
    oraMin(true, true, false),
    lista(true, true, false),
    check(false, true, false),

    boolean richiesto
    boolean modificabile
    boolean linkato

    Field(boolean richiesto, boolean modificabile, boolean linkato) {
        this.richiesto = richiesto
        this.modificabile = modificabile
        this.linkato = linkato
    }// fine del metodo costruttore

}// fine della classe Enumeration
