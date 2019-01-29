package webambulanze

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 3-6-13
 * Time: 19:59
 */
public enum TipoViaggio {

    auto118('118', 'Servizio emergenza-urgenza del 118'),
    ordinario('ord', 'Servizio ambulanza ordinario'),
    dializzati('dia', 'Servizio trasporto dializzati'),
    interno('int', 'Servizio interno')

    String sigla
    String nome

    TipoViaggio(String sigla, String nome) {
        this.sigla = sigla
        this.nome = nome
    }// fine del metodo costruttore

    /**
     * valore di testo restituito per una istanza della classe
     */
    String toString() {
        return sigla
    } // end of toString

    public static ArrayList getListaSigla() {
        ArrayList lista = new ArrayList()
        String nome

        values()?.each {
            nome = it.sigla
            lista.add(nome)
        }// fine del ciclo each

        return lista
    }// fine del metodo statico

    public static ArrayList getListaNome() {
        ArrayList lista = new ArrayList()
        String nome

        values()?.each {
            nome = it.nome
            lista.add(nome)
        }// fine del ciclo each

        return lista
    }// fine del metodo statico

    public static TipoViaggio getDaSigla(String sigla) {
        TipoViaggio tipoViaggio = null
        String siglaCorrente

        values()?.each {
            siglaCorrente = it.sigla
            if (siglaCorrente.equals(sigla)) {
                tipoViaggio = it
            }// fine del blocco if
        }// fine del ciclo each

        return tipoViaggio
    }// fine del metodo statico

}// fine della classe Enumeration
