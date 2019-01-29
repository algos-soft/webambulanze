package webambulanze

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 19-12-13
 * Time: 07:10
 */
public enum Festivi {

    capodanno(1, 1, 1, 1, 1, 1, 1),
    epifania(6, 6, 6, 6, 6, 6, 6),
    carnevale(65, 50, 41, 0, 46, 38, 57),
    pasqua(114, 99, 90, 110, 95, 87, 106),
    pasquetta(115, 100, 91, 111, 96, 88, 107),
    liberazione(115, 116, 115, 115, 115, 115, 115),
    lavoro(121, 122, 121, 121, 121, 121, 121),
    repubblica(153, 154, 153, 153, 153, 153, 153),
    ferragosto(227, 228, 227, 227, 227, 227, 227),
    ognissanti(305, 306, 305, 305, 305, 305, 305),
    immacolata(342, 343, 342, 342, 342, 342, 342),
    natale(359, 360, 359, 359, 359, 359, 359),
    stefano(360, 361, 360, 360, 360, 360, 360)

    private int anno11
    private int anno12
    private int anno13
    private int anno14
    private int anno15
    private int anno16
    private int anno17

    /**
     * Costruttore con parametri.
     */
    Festivi(int anno11, int anno12, int anno13, int anno14, int anno15, int anno16, int anno17) {
        this.setAnno11(anno11)
        this.setAnno12(anno12)
        this.setAnno13(anno13)
        this.setAnno14(anno14)
        this.setAnno15(anno15)
        this.setAnno16(anno16)
        this.setAnno17(anno17)
    }// fine del metodo costruttore

    //--restituisce tutti e solo i giorni festivi dell'anno
    static public ArrayList<Integer> all2011() {
        ArrayList<Integer> giorni = new ArrayList<Integer>()
        ArrayList<Festivi> festivi = values()
        Festivi festivo
        int numProgressivoGiorno

        festivi.each {
            festivo = it
            numProgressivoGiorno = festivo.getAnno11()

            if (numProgressivoGiorno > 0) {
                giorni.add(numProgressivoGiorno)
            }// fine del blocco if
        } // fine del ciclo each

        return giorni
    }// fine del metodo statico

    //--restituisce tutti e solo i giorni festivi dell'anno
    static public ArrayList<Integer> all2012() {
        ArrayList<Integer> giorni = new ArrayList<Integer>()
        ArrayList<Festivi> festivi = values()
        Festivi festivo
        int numProgressivoGiorno

        festivi.each {
            festivo = it
            numProgressivoGiorno = festivo.getAnno12()

            if (numProgressivoGiorno > 0) {
                giorni.add(numProgressivoGiorno)
            }// fine del blocco if
        } // fine del ciclo each

        return giorni
    }// fine del metodo statico

    //--restituisce tutti e solo i giorni festivi dell'anno
    static public ArrayList<Integer> all2013() {
        ArrayList<Integer> giorni = new ArrayList<Integer>()
        ArrayList<Festivi> festivi = values()
        Festivi festivo
        int numProgressivoGiorno

        festivi.each {
            festivo = it
            numProgressivoGiorno = festivo.getAnno13()

            if (numProgressivoGiorno > 0) {
                giorni.add(numProgressivoGiorno)
            }// fine del blocco if
        } // fine del ciclo each

        return giorni
    }// fine del metodo statico

    //--restituisce tutti e solo i giorni festivi dell'anno
    static public ArrayList<Integer> all2014() {
        ArrayList<Integer> giorni = new ArrayList<Integer>()
        ArrayList<Festivi> festivi = values()
        Festivi festivo
        int numProgressivoGiorno

        festivi.each {
            festivo = it
            numProgressivoGiorno = festivo.getAnno14()

            if (numProgressivoGiorno > 0) {
                giorni.add(numProgressivoGiorno)
            }// fine del blocco if
        } // fine del ciclo each

        return giorni
    }// fine del metodo statico

    //--restituisce tutti e solo i giorni festivi dell'anno
    static public ArrayList<Integer> all2015() {
        ArrayList<Integer> giorni = new ArrayList<Integer>()
        ArrayList<Festivi> festivi = values()
        Festivi festivo
        int numProgressivoGiorno

        festivi.each {
            festivo = it
            numProgressivoGiorno = festivo.getAnno15()

            if (numProgressivoGiorno > 0) {
                giorni.add(numProgressivoGiorno)
            }// fine del blocco if
        } // fine del ciclo each

        return giorni
    }// fine del metodo statico

    //--restituisce tutti e solo i giorni festivi dell'anno
    static public ArrayList<Integer> all2016() {
        ArrayList<Integer> giorni = new ArrayList<Integer>()
        ArrayList<Festivi> festivi = values()
        Festivi festivo
        int numProgressivoGiorno

        festivi.each {
            festivo = it
            numProgressivoGiorno = festivo.getAnno16()

            if (numProgressivoGiorno > 0) {
                giorni.add(numProgressivoGiorno)
            }// fine del blocco if
        } // fine del ciclo each

        return giorni
    }// fine del metodo statico

    //--restituisce tutti e solo i giorni festivi dell'anno
    static public ArrayList<Integer> all2017() {
        ArrayList<Integer> giorni = new ArrayList<Integer>()
        ArrayList<Festivi> festivi = values()
        Festivi festivo
        int numProgressivoGiorno

        festivi.each {
            festivo = it
            numProgressivoGiorno = festivo.getAnno17()

            if (numProgressivoGiorno > 0) {
                giorni.add(numProgressivoGiorno)
            }// fine del blocco if
        } // fine del ciclo each

        return giorni
    }// fine del metodo statico

    //--restituisce tutti e solo i giorni festivi dell'anno
    static public ArrayList<Integer> all(String anno) {
        ArrayList<Integer> giorni = new ArrayList<Integer>()
        ArrayList anniValidi = ['2011', '2012', '2013', '2014', '2015', '2016', '2017']

        if (anno && anno in anniValidi) {
            switch (anno) {
                case anniValidi[0]:
                    giorni = all2011()
                    break
                case anniValidi[1]:
                    giorni = all2012()
                    break
                case anniValidi[2]:
                    giorni = all2013()
                    break
                case anniValidi[3]:
                    giorni = all2014()
                    break
                case anniValidi[4]:
                    giorni = all2015()
                    break
                case anniValidi[5]:
                    giorni = all2016()
                    break
                case anniValidi[6]:
                    giorni = all2017()
                    break
                default: // caso non definito
                    break
            } // fine del blocco switch
        }// fine del blocco if

        return giorni
    }// fine del metodo statico

    int getAnno11() {
        return anno11
    }

    void setAnno11(int anno11) {
        this.anno11 = anno11
    }

    int getAnno12() {
        return anno12
    }

    void setAnno12(int anno12) {
        this.anno12 = anno12
    }

    int getAnno13() {
        return anno13
    }

    void setAnno13(int anno13) {
        this.anno13 = anno13
    }

    int getAnno14() {
        return anno14
    }

    void setAnno14(int anno14) {
        this.anno14 = anno14
    }

    int getAnno15() {
        return anno15
    }

    void setAnno15(int anno15) {
        this.anno15 = anno15
    }

    int getAnno16() {
        return anno16
    }

    void setAnno16(int anno16) {
        this.anno16 = anno16
    }

    int getAnno17() {
        return anno17
    }

    void setAnno17(int anno17) {
        this.anno17 = anno17
    }
} // fine della classe
