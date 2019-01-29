package webambulanze

import java.sql.Timestamp
import java.text.DateFormatSymbols

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 28-12-12
 * Time: 09:01
 */
class Lib {
    private static String aCapo = '\n'
    private static String tab1 = '  '
    private static String tab2 = tab1 + tab1
    private static String tab3 = tab2 + tab1

    private static String colBack = 'ff4444'
    private static String colText = '444444'

    /**
     * tag per il valore falso per una posizione
     */
    public static final int INT_NULLO = -1

    static String getTitoloPagina(String testoIn) {
        String testoOut = ''
        int alt = 40
        testoOut += '<div style="padding:10px;font-size:30px;height:'
        testoOut += alt
        testoOut += 'px;background:#'
        testoOut += colBack
        testoOut += ';color:#'
        testoOut += colText
        testoOut += '">'
//        testoOut += '<img src="file:///Macintosh HD/Users/Gac/Documents/IdeaProjects/webambulanze/web-app/images/skin/database_add.png" width="40" height="40" alt="CRI"/>'
//        testoOut += '<img src="/Users/Gac/Desktop/Immagini/FacciaPositiva.png" width="40" height="40" alt="CRI"/>'

        testoOut += testoIn
        testoOut += '</div>'

        testoOut = '<div class="titolopagina">Pippo</div>'
        return testoOut
    }// fine del metodo

    static String test(String testoIn) {
        String testo = ''

        testo += '<table width="90%">'
        testo += '<thead>'
        testo += '<tr>'
//        testo += '<th align="left" valign="middle" color="#444444" bgcolor="#ff4444">'
        testo += '<th bgcolor=#ff4444>'
//        testo+='<span class="Stile1 Stile2">'
        testo += testoIn
//        testo+='</span>'
        testo += '</th>'
//        testo+='<th width="150; align="left" valign="middle" bordercolor="#00CCCC" bgcolor="#ff4444">'
//        testo+='Mario Draghi'
        testo += '</tr>'
        testo += '</thead>'
//        testo+='<th width="150; align="left" valign="middle" bordercolor="#00CCCC" bgcolor="#ff4444">'
//        testo+='is logged as...'
//        testo+='</th>'
//        testo+='</tr>'
        testo += '</table>'

        return testo
    }// fine del metodo

    static String tagTable(String testoIn) {
        return tagTable(testoIn, null)
    }// fine del metodo

    static String tagTable(String testoIn, Aspetto cella) {
        if (cella) {
            return tag('table', testoIn, cella.toString(), '', 0, true)
        } else {
            return tag('table', testoIn, '', '', 0, true)
        }// fine del blocco if-else
    }// fine del metodo

    static String getCaption(String testoIn) {
        String testoOut = tab1

        testoOut += '<caption>'
        testoOut += aCapo
        testoOut += tab2
        testoOut += '<div align="center">'
        testoOut += '<b>'
        testoOut += testoIn
        testoOut += '</b>'
        testoOut += '</div>'
        testoOut += aCapo
        testoOut += tab1
        testoOut += '</caption>'
        testoOut += aCapo

        return testoOut
    }// fine del metodo

    static String tagCaption(String testoIn) {
        return tagCaption(testoIn, null)
    }// fine del metodo

    static String tagCaption(String testoIn, Aspetto cella) {
        if (cella) {
            return tag('caption', testoIn, cella.toString(), '', 0)
        } else {
            return tag('caption', testoIn, '', '', 0)
        }// fine del blocco if-else
    }// fine del metodo

    static String tagHead(String testoIn) {
        String testoOut = ''

        testoOut += '<thead>'
        testoOut += aCapo
        testoOut += testoIn.trim()
        testoOut += aCapo
        testoOut += '</thead>'
        testoOut += aCapo

        return testoOut
    }// fine del metodo

    static String tagRiga(String testoIn) {
        String testoOut = ''

        testoOut += '<tr>'
        testoOut += aCapo
        testoOut += testoIn.trim()
        testoOut += aCapo
        testoOut += '</tr>'
        testoOut += aCapo

        return testoOut
    }// fine del metodo

    static String getBody(String testoIn) {
        String testoOut = ''

        testoOut += '<tbody>'
        testoOut += aCapo
        testoOut += testoIn.trim()
        testoOut += aCapo
        testoOut += '</tbody>'
        testoOut += aCapo

        return testoOut
    }// fine del metodo

    static String getBold(String testoIn) {
        String testoOut = ''

        testoOut += '<bold>'
        testoOut += testoIn
        testoOut += '</bold>'

        return testoOut
    }// fine del metodo

    static String getTitoloBase(String testoIn, String colore, int span) {
        String testoOut = tab3

        testoOut += '<th'
        if (span) {
            testoOut += ' colspan="'
            testoOut += span
            testoOut += '"'
        }// fine del blocco if
        if (colore) {
            testoOut += ' bgcolor=#'
            testoOut += colore
        }// fine del blocco if
        testoOut += '>'

        if (colore) {
            testoOut += '<FONT FACE="Geneva, Arial" SIZE=2>'
        }// fine del blocco if

        testoOut += testoIn
        testoOut += '</th>'
        testoOut += aCapo

        return testoOut
    }// fine del metodo

    public static String getCellaBase(String testoIn, String colore, int span) {
        String testoOut = tab3

        testoOut += '<td class="tab"'
        if (span) {
            testoOut += ' rowspan="'
            testoOut += span
            testoOut += '"'
        }// fine del blocco if-else
        if (colore) {
            testoOut += ' bgcolor=#'
            testoOut += colore
        }// fine del blocco if-else
        testoOut += '>'

        testoOut += testoIn
        testoOut += '</td>'
        testoOut += aCapo

        return testoOut
    }// fine del metodo

    static String getTitolo(String testoIn, String colore) {
        return getTitoloBase(testoIn, colore, 0)
    }// fine del metodo

    static String getTitolo(String testoIn) {
        return getTitolo(testoIn, '')
    }// fine del metodo

    static String getTitoloTabellaSorted(String app, String cont, String sort, String order, String title) {
        return "<th class=\"sortable\"><a href=\"/${app}/${cont}/list?sort=${sort}&amp;order=${order}\">${title}</a></th>"
    }// fine del metodo

    static String getTitoloTabellaNotSorted(String app, String cont, String sort, String order, String title) {
        String testo

        if (order && order.equals('desc')) {
            testo = "<th class=\"sortable sorted desc\"><a href=\"/${app}/${cont}/list?sort=${sort}&amp;order=${order}\">${title}</a></th>"
        } else {
            testo = "<th class=\"sortable sorted asc\"><a href=\"/${app}/${cont}/list?sort=${sort}&amp;order=${order}\">${title}</a></th>"
        }// fine del blocco if-else

        return testo
//        return "<th class=\"sortable\"><a href=\"/${app}/${cont}/list?sort=${sort}&amp;order=${order}\">${title}</a></th>"
    }// fine del metodo

    static String getCampoTabellaInt(String ref, def value) {
        String testo = ''

        testo += ref
        testo += "${value}"
        testo += "</a>"

        return tagCella(testo)
    }// fine del metodo

    static String getCampoTabellaLong(String ref, def value) {
        String testo = ''

        testo += ref
        testo += "${value}"
        testo += "</a>"

        return Lib.tagCella(testo)
    }// fine del metodo

    static String getCampoTabellaBooleano(boolean value) {
        String testo = ''

        if (value) {
            testo = "<input type=\"checkbox\" checked>"
        } else {
            testo = "<input type=\"checkbox\" disabled>"
        }// fine del blocco if-else

        return Lib.tagCella(testo)
    }// fine del metodo

    static String getCampoTabellaStringa(String ref, def value) {
        String testo = ''
        String valore = "${value}"

        testo += ref
        testo += "${value}"
        testo += "</a>"

        if (valore.equals(Cost.STATUS_VERDE) || valore.equals(Cost.STATUS_ROSSO)) {
            if (valore.equals(Cost.STATUS_VERDE)) {
                return Lib.tagCella(valore, Aspetto.verde)
            } else {
                return Lib.tagCella(valore, Aspetto.rosso)
            }// fine del blocco if-else
        } else {
            return Lib.tagCella(testo)
        }// fine del blocco if-else

    }// fine del metodo

    static String getCampoTabellaData(String app, String cont, long id, def value) {
        String testo
        String dataTxt = Lib.presentaDataMese(value)

        testo = "<a href=\"/${app}/${cont}/show/${id}\">${dataTxt}</a>"
        return Lib.tagCella(testo)
    }// fine del metodo

    static String getCampoTabellaTime(String app, String cont, long id, def value) {
        String testo
        String timeTxt = value
        timeTxt = timeTxt.substring(0, 16)

        if (isData(value)) {
            testo = getCampoTabellaData(app, cont, id, value)
        } else {
            testo = "<a href=\"/${app}/${cont}/show/${id}\">${timeTxt}</a>"
            testo = Lib.tagCella(testo)
        }// fine del blocco if-else

        return testo
    }// fine del metodo

    static boolean isData(def value) {
        boolean isData = false
        String tag0 = '0'
        String tag00 = '00'
        String timeTxt = value
        String ore = timeTxt.substring(11, 13)
        String minuti = timeTxt.substring(14, 16)
        String secondi = timeTxt.substring(17, 19)
        String decimi = timeTxt.substring(20, 21)

        if (ore.equals(tag00) && minuti.equals(tag00) && secondi.equals(tag00) && decimi.equals(tag0)) {
            isData = true
        }// fine del blocco if

        return isData
    }// fine del metodo

    static String getCampoTabella(String app, String cont, long id, def value) {
        return getCampoTabella(app, cont, id, value, 'show')
    }// fine del metodo

    static String getCampoTabella(String app, String cont, long id, def value, String action) {
        String testo = ''
        String ref = "<a href=\"/${app}/${cont}/${action}/${id}\">"

        if (value instanceof Integer || value instanceof Long || value instanceof Boolean || value instanceof String || value instanceof Date) {

            if (value instanceof Integer) {
                testo = getCampoTabellaInt(ref, value)
            }// fine del blocco if

            if (value instanceof Long) {
                testo = getCampoTabellaLong(ref, value)
            }// fine del blocco if

            if (value instanceof Boolean) {
                testo = getCampoTabellaBooleano(value)
            }// fine del blocco if

            if (value instanceof String) {
                testo = getCampoTabellaStringa(ref, value)
            }// fine del blocco if

            if (value instanceof Date) {
                if (value instanceof Timestamp) {
                    testo = getCampoTabellaTime(app, cont, id, value)
                    testo = getCampoTabellaData(app, cont, id, value)
                } else {
                    testo = getCampoTabellaData(app, cont, id, value)
                }// fine del blocco if-else
            }// fine del blocco if
        } else {
            if (value) {
                testo = getCampoTabellaStringa(ref, value)
            } else {
                testo = getCampoTabellaStringa(ref, '')
            }// fine del blocco if-else
        }// fine del blocco if-else

        return testo
    }// fine del metodo

    static String getCampoSchedaBooleano(String nome, String testoLabel, boolean flag) {
        String testoOut = ''

        //--prima la label
        testoOut += Lib.tagLabel(nome, testoLabel)

//        //--poi il campo col checkbox
//        testoOut += "<input type=\"hidden\" name=\"_${nome}\" /><input type=\"checkbox\""
//        if (value) {
//            testoOut += ' checked '
//        } else {
//            testoOut += ' disabled '
//        }// fine del blocco if-else
//        testoOut += "name=\"\${nome}\" id=\"\${nome}\"/>"

        //--poi il campo col valore=vero/falso
        testoOut += Lib.tagBool(nome, flag)

        //--il tutto inserito nel tag tagLiContain
        testoOut = Lib.tagLiContain(testoOut)

        //--il tutto inserito nel tag tagOlClass
        testoOut = Lib.tagOlClass(testoOut)

        return testoOut
    }// fine del metodo


    static String getCampoSchedaFormBooleano(String nome, String testoLabel, boolean flag) {
        String testoOut = ''

        //--prima la label
        testoOut += Lib.tagLabelEdit(nome, testoLabel)

        //--poi il blocco hidden
        testoOut += Lib.tagHidden(nome)

        //--poi il blocco checkbox
        testoOut += Lib.tagCheckbox(nome, flag)

        //--il tutto inserito nel tag tagLiContain
        testoOut = Lib.tagDivContain(testoOut)

        return testoOut
    }// fine del metodo

    /**
     * Forza il primo carattere della stringa a maiuscolo
     * Restituisce una stringa
     * Un numero ritorna un numero
     * Un nullo ritorna un nullo
     * Un oggetto non stringa ritorna uguale
     *
     * @param entrata stringa in ingresso
     * @return uscita string in uscita
     */
    public static primaMaiuscola(entrata) {
        // variabili e costanti locali di lavoro
        def uscita = entrata
        String primo
        String rimanente

        if (entrata && entrata in String) {
            primo = entrata.substring(0, 1)
            primo = primo.toUpperCase()
            rimanente = entrata.substring(1)
            uscita = primo + rimanente.toLowerCase()
        }// fine del blocco if

        // valore di ritorno
        return uscita
    } // fine del metodo statico

    //--Inserisce il testo nel tag della cella (titolo)
    public static tagCellaTitolo(String testoIn) {
        return tagCellaTitolo(testoIn, null)
    } // fine del metodo statico

    //--Inserisce il testo nel tag della cella (titolo)
    public static tagCellaTitolo(String testoIn, Aspetto classe) {
        return tagCellaTitolo(testoIn, classe, 1)
    } // fine del metodo statico

    //--Inserisce il testo nel tag della cella (titolo)
    public static tagCellaTitolo(String testoIn, Aspetto classe, int span) {
        return tag('th', testoIn, classe.toString(), 'col', span)
    } // fine del metodo statico

    //--Inserisce il testo nel tag della cella (normale)
    public static tagCella(String testoIn) {
        return tagCella(testoIn, null)
    } // fine del metodo statico

    //--Inserisce il testo nel tag della cella (normale)
    public static tagCella(String testoIn, Aspetto classe) {
        return tagCella(testoIn, classe, 1)
    } // fine del metodo statico

    //--Inserisce il testo nel tag della cella (normale)
    public static tagCella(String testoIn, Aspetto classe, int span) {
        return tag('td', testoIn, classe.toString(), 'row', span)
    } // fine del metodo statico

    //--Inserisce il testo nel tag di un elemento HTML
    //--Sulla stessa riga HTML
    public static tag(String tag, String testoIn, String classe, String prefixSpan, int span) {
        return Lib.tag(tag, testoIn, classe, prefixSpan, span, false)
    } // fine del metodo statico

    //--Inserisce il testo nel tag di un elemento HTML
    //--Sulla stessa riga HTML
    //--Sulla righe separate
    public static tag(String tag, String testoIn, String classe, String prefixSpan, int span, boolean righeSeparate) {
        String testoOut = ''

        testoOut += "<${tag}"
        if (classe && !classe.equals('null')) {
            testoOut += ' class="'
            testoOut += classe
            testoOut += '"'
        }// fine del blocco if

        if (span && span > 1) {
            testoOut += " ${prefixSpan}span=\""
            testoOut += span
            testoOut += '"'
        }// fine del blocco if
        testoOut += '>'

        if (righeSeparate) {
            testoOut += aCapo
        }// fine del blocco if

        testoOut += testoIn
        if (righeSeparate) {
            testoOut += aCapo
        }// fine del blocco if
        testoOut += "</${tag}>"

        return testoOut
    } // fine del metodo statico

    //--Inserisce il testo nel tag div
    public static tagDiv(String testoIn) {
        String testoOut = ''

        if (testoIn) {
            testoOut += '<div>'
            testoOut += testoIn
            testoOut += '</div>'
        }// fine del blocco if

        return testoOut
    } // fine del metodo statico

    //--Inserisce il testo nel tag div, formattato a destra
    public static tagDivDex(String testoIn) {
        String testoOut = ''

        if (testoIn) {
            testoOut += '<div style="text-align: right;">'
            testoOut += testoIn
            testoOut += '</div>'
        }// fine del blocco if

        return testoOut
    } // fine del metodo statico

    //--Inserisce il testo nel tag div contain
    public static tagLiContain(String testoIn) {
        String testoOut = ''

        if (testoIn) {
            testoOut += '<li class="fieldcontain">'
            testoOut += testoIn
            testoOut += '</li>'
        }// fine del blocco if

        return testoOut
    } // fine del metodo statico

    //--Inserisce il testo nel tag div contain
    public static tagDivContain(String testoIn) {
        String testoOut = ''

        if (testoIn) {
            testoOut += '<div class="fieldcontain">'
            testoOut += testoIn
            testoOut += '</div>'
        }// fine del blocco if

        return testoOut
    } // fine del metodo statico

    //--Costruisce il tag label per il testo indicato
    public static tagLabel(String nome, String testoLabel) {
        String testoOut = ''

        if (nome && testoLabel) {
            testoLabel = Lib.primaMaiuscola(testoLabel)
            testoOut += "<span id=\"${nome}-label\" class=\"property-label\">"
            testoOut += testoLabel
            testoOut += '</span>'
        }// fine del blocco if

        return testoOut
    } // fine del metodo statico

    //--Costruisce il tag label per il testo indicato
    public static tagLabelEdit(String nome, String testoLabel) {
        String testoOut = ''

        if (nome) {
            testoLabel = Lib.primaMaiuscola(testoLabel)
            testoOut += "<label for=\"${nome}\">"
            testoOut += testoLabel
            testoOut += '</label>'
        }// fine del blocco if

        return testoOut
    } // fine del metodo statico

    //--Costruisce il tag booleano mostrando vero o falso
    public static tagBool(String testoIn, boolean flag) {
        String testoOut = ''

        testoOut += "<span class=\"property-value\" aria-labelledby=\"${testoIn}-label\">"
        if (flag) {
            testoOut += 'Vero'
        } else {
            testoOut += 'Falso'
        }// fine del blocco if-else
        testoOut += "</span>"

        return testoOut
    } // fine del metodo statico

    //--Costruisce il tag ol per il testo indicato
    public static tagOlClass(String testoIn) {
        String testoOut = ''

        if (testoIn) {
            testoOut += '<ol class="property-list milite">'
            testoOut += testoIn
            testoOut += '</ol>'
        }// fine del blocco if

        return testoOut
    } // fine del metodo statico

    //--Costruisce il tag hidden per il testo indicato
    public static tagHidden(String nome) {
        String testoOut = ''

        if (nome) {
            testoOut += '<input type="hidden" name="_'
            testoOut += nome
            testoOut += '"/>'
        }// fine del blocco if

        return testoOut
    } // fine del metodo statico

    //--Costruisce il tag checkbox per il testo indicato
    public static tagCheckbox(String nome, boolean flag) {
        String testoOut = ''

        if (nome) {
            testoOut += '<input type="checkbox" name="'
            testoOut += nome
            testoOut += '"'
            if (flag) {
                testoOut += ' checked="checked" '
            }// fine del blocco if
            testoOut += '" id="'
            testoOut += nome
            testoOut += '"/>'
        }// fine del blocco if

        return testoOut
    } // fine del metodo statico

    public static Turno creaTurno(Croce croce, TipoTurno tipoTurno, Date giorno) {
        Turno turno = null
        Date primoGennaio = creaData1Gennaio(giorno)
        Date inizio
        Date fine
        int offSet = getNumGiorno(giorno) - 1
        inizio = primoGennaio + offSet
        fine = primoGennaio + offSet
        String funz

        inizio = setOra(inizio, tipoTurno.oraInizio)
        inizio = setMinuto(inizio, tipoTurno.minutiInizio)
        fine = setOra(fine, tipoTurno.oraFine)
        fine = setMinuto(fine, tipoTurno.minutiFine)
        if (tipoTurno.fineGiornoSuccessivo) {
            fine = fine + 1
        }// fine del blocco if

        //turno = Turno.findOrCreateByCroceAndTipoTurnoAndGiornoAndInizioAndFine(croce, tipoTurno, giorno, inizio, fine)
        if (tipoTurno.multiplo) {
            turno = new Turno(croce: croce, tipoTurno: tipoTurno, giorno: giorno)
        } else {
            turno = Turno.findOrCreateByCroceAndTipoTurnoAndGiorno(croce, tipoTurno, giorno)
        }// fine del blocco if-else
        turno.inizio = inizio
        turno.fine = fine
        for (int k = 1; k <= tipoTurno.getListaFunzioni().size(); k++) {
            funz = 'funzione' + k
            turno."${funz}" = tipoTurno.getListaFunzioni().get(k - 1)
        } // fine del ciclo for
        turno.note = ''
        turno.localitàExtra = ''
        turno.save(failOnError: true)

        return turno
    }// fine del metodo

    /**
     * Presentazione della data.
     */
    def static presentaDataNum(Date data) {
        /* variabili e costanti locali di lavoro */
        String dataFormattata = ''
        GregorianCalendar cal = new GregorianCalendar()
        def giorno
        def mese
        def anno
        String sep = '-'

        try { // prova ad eseguire il codice
            if (data) {
                cal.setTime(data)
                giorno = cal.get(Calendar.DAY_OF_MONTH)
                mese = cal.get(Calendar.MONTH)
                mese++
                anno = cal.get(Calendar.YEAR)
                anno = anno + ''
                anno = anno.substring(2)

                dataFormattata += giorno
                dataFormattata += sep
                dataFormattata += mese
                dataFormattata += sep
                dataFormattata += anno
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return dataFormattata
    }// fine del metodo

    /**
     * Presentazione della data.
     */
    def static presentaDataMese(Date data) {
        /* variabili e costanti locali di lavoro */
        String dataFormattata = ''
        GregorianCalendar cal = new GregorianCalendar()
        def giorno
        def mese
        def anno
        String sep = '-'

        try { // prova ad eseguire il codice
            if (data) {
                cal.setTime(data)
                giorno = cal.get(Calendar.DAY_OF_MONTH)
                mese = cal.get(Calendar.MONTH)
                mese++
                mese = Mese.getShort(mese)  //scrive il nome del mese, ma allarga la colonna
                anno = cal.get(Calendar.YEAR)
                anno = anno + ''
                anno = anno.substring(2)

                dataFormattata += giorno
                dataFormattata += sep
                dataFormattata += mese
                dataFormattata += sep
                dataFormattata += anno
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return dataFormattata
    }// fine del metodo

    /**
     * Restituisce il giorno della settimana.
     * <p/>
     * Giorno scritto per intero <br>
     *
     * @param giorno da elaborare
     *
     * @return giorno della settimana
     */
    public static String getGiorno(Date giorno) {
        /* variabili e costanti locali di lavoro */
        String settimana = ""
        int pos
        GregorianCalendar cal = new GregorianCalendar()

        try { // prova ad eseguire il codice
            if (giorno) {
                cal.setTime(giorno);
                pos = cal.get(Calendar.DAY_OF_WEEK);

                // la settimana inglese comincia da domenica
                // quella italiana da lunedi

                pos--;

                // shift della domenica
                if (pos == 0) {
                    pos = 7;
                }// fine del blocco if

                // nel calendario i giorni della settimana cominciano da 1
                // la Enumeration comincia da zero
                pos--;

                if ((pos >= 0) && (pos <= 7)) {
                    settimana = Giorno.values()[pos].toString();
                }// fine del blocco if
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return settimana;
    }

    /**
     * Presentazione della data.
     */
    def static presentaDataCompleta(Date data) {
        /* variabili e costanti locali di lavoro */
        String dataFormattata = ''
        GregorianCalendar cal = new GregorianCalendar()
        def giorno
        def mese
        def anno
        String sep = '-'
        String settimana = getGiorno(data)

        try { // prova ad eseguire il codice
            if (data) {
                cal.setTime(data)
                giorno = cal.get(Calendar.DAY_OF_MONTH)
                mese = cal.get(Calendar.MONTH)
                mese++
                mese = Mese.getShort(mese)  //scrive il nome del mese, ma allarga la colonna
                anno = cal.get(Calendar.YEAR)

                dataFormattata += settimana
                dataFormattata += ', '
                dataFormattata += giorno
                dataFormattata += sep
                dataFormattata += mese
                dataFormattata += sep
                dataFormattata += anno
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return dataFormattata
    }// fine del metodo

    public static String presentaOraData(Date data) {
        String testo = ''
        GregorianCalendar cal = new GregorianCalendar()
        String settimana = getGiorno(data)
        def mese
        int numOra = getNumOra(data)
        int numMinuti = getNumMinuti(data)
        int numGiorno = getNumGiornoMese(data)
        int numMese = getNumMese(data)
        String numOraTxt
        String numMinutiTxt

        numOraTxt = numOra + ''
        if (numOraTxt.length() == 1) {
            numOraTxt = '0' + numOraTxt
        }// fine del blocco if

        numMinutiTxt = numMinuti + ''
        if (numMinutiTxt.length() == 1) {
            numMinutiTxt = '0' + numMinutiTxt
        }// fine del blocco if

        cal.setTime(data)
        mese = cal.get(Calendar.MONTH)
        mese++
        mese = Mese.getLong(mese)  //scrive il nome del mese, ma allarga la colonna

        testo += numOraTxt
        testo += ':'
        testo += numMinutiTxt
        testo += ' di '
        testo += settimana
        testo += ', '
        testo += numGiorno
        testo += ' '
        testo += mese

        return testo
    }// fine del metodo

    /**
     * Crea la data del primo gennaio corrente anno.
     * <p/>
     *
     * @param giorno il giorno del mese (1 per il primo)
     * @param mese il mese dell'anno (1 per gennaio)
     * @param anno l'anno
     *
     * @return la data creata
     */
    public static Date creaData1Gennaio() {
        /* variabili e costanti locali di lavoro */
        Date primoGennaio = new Date()
        Calendar cal

        try { // prova ad eseguire il codice
            cal = Calendar.getInstance()
            cal.setTime(primoGennaio)
            cal.set(Calendar.MONTH, 0)
            cal.set(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            primoGennaio = new Date(cal.getTime().getTime());

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return primoGennaio
    }// fine del metodo

    /**
     * Crea la data del primo gennaio dell'anno indicato.
     * @return 1° gennaio dell'anno indicato
     */
    public static Date creaData1Gennaio(String anno) {
        /* variabili e costanti locali di lavoro */
        Date primoGennaio
        Calendar cal

        cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, Integer.decode(anno))
        cal.set(Calendar.MONTH, 0)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        primoGennaio = new Date(cal.getTime().getTime());

        /* valore di ritorno */
        return primoGennaio
    }// fine del metodo

    /**
     * Crea la data dell'ultimo dell'anno indicato.
     * @return 31 dicembre dell'anno indicato
     */
    public static Date creaData31Dicembre(String anno) {
        /* variabili e costanti locali di lavoro */
        Date trentunDicembre
        Calendar cal

        cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, Integer.decode(anno))
        cal.set(Calendar.MONTH, 11)
        cal.set(Calendar.DAY_OF_MONTH, 31)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        trentunDicembre = new Date(cal.getTime().getTime());

        /* valore di ritorno */
        return trentunDicembre
    }// fine del metodo

    /**
     * Crea la data del primo gennaio del giorno indicato.
     * @return 1° gennaio dell'anno a cui appartiene il giorno indicato
     */
    public static Date creaData1Gennaio(Date giorno) {
        return creaData1Gennaio(getAnno(giorno))
    }// fine del metodo

    /**
     * Crea la data del giorno.
     *
     * @return la data creata
     */
    public static Date creaData(int num) {
        /* variabili e costanti locali di lavoro */
        Date giorno = null

        try { // prova ad eseguire il codice
            giorno = creaData1Gennaio()
            giorno = giorno + num - 1
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return giorno
    }// fine del metodo

    /**
     * Crea la data del giorno.
     *
     * @return la data creata
     */
    public static Date creaDataOggi() {
        /* variabili e costanti locali di lavoro */
        Date giorno = new Date()
        Calendar cal

        try { // prova ad eseguire il codice
            cal = Calendar.getInstance()
            cal.setTime(giorno)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            giorno = new java.util.Date(cal.getTime().getTime());

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return giorno
    }// fine del metodo

    /**
     * Crea la data
     *
     * FORMAT: gg/mm/aaaa
     *
     * @return la data creata
     */
    public static Date creaData(String dataSottoFormaDiTesto) {
        /* variabili e costanti locali di lavoro */
        Date data = null
        Calendar cal
        String sep = '/'
        int posPrimoSep
        int posSecondoSep
        String giornoTxt = ''
        String meseTxt = ''
        String annoTxt = ''
        int giorno = 0
        int mese = 0
        int anno = 0

        try { // prova ad eseguire il codice
            posPrimoSep = dataSottoFormaDiTesto.indexOf(sep)
            posSecondoSep = dataSottoFormaDiTesto.lastIndexOf(sep)
            giornoTxt = dataSottoFormaDiTesto.substring(0, posPrimoSep)
            meseTxt = dataSottoFormaDiTesto.substring(++posPrimoSep, posSecondoSep)
            annoTxt = dataSottoFormaDiTesto.substring(++posSecondoSep)
            giorno = Integer.decode(giornoTxt)
            mese = Integer.decode(meseTxt)
            if (mese > 0) {
                mese--
            }// fine del blocco if
            anno = Integer.decode(annoTxt)

            cal = Calendar.getInstance()

            cal.set(Calendar.DAY_OF_MONTH, giorno)
            cal.set(Calendar.MONTH, mese)
            cal.set(Calendar.YEAR, anno)

            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            data = new Date(cal.getTime().getTime());

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return data
    }// fine del metodo


    public static Date setOra(Date giornoIn, int ora) {
        Date giornoOut
        Calendar cal = Calendar.getInstance()

        cal.setTime(giornoIn)
        cal.set(Calendar.HOUR_OF_DAY, ora)

        giornoOut = new Date(cal.getTime().getTime());

        return giornoOut
    }// fine del metodo

    public static Date setMinuto(Date giornoIn, int minuto) {
        Date giornoOut
        Calendar cal = Calendar.getInstance()

        cal.setTime(giornoIn)
        cal.set(Calendar.MINUTE, minuto)

        giornoOut = new Date(cal.getTime().getTime());

        return giornoOut
    }// fine del metodo


    public static Date setSecondo(Date giornoIn, int secondo) {
        Date giornoOut
        Calendar cal = Calendar.getInstance()

        cal.setTime(giornoIn)
        cal.set(Calendar.SECOND, secondo)

        giornoOut = new Date(cal.getTime().getTime());

        return giornoOut
    }// fine del metodo

    public static String getAnno(Date giorno) {
        String anno
        Calendar cal = Calendar.getInstance()

        cal.setTime(giorno)
        anno = cal.get(Calendar.YEAR)

        return anno
    }// fine del metodo

    public static int getNumGiorno(Date giornoIn) {
        int giorno
        Calendar cal = Calendar.getInstance()

        cal.setTime(giornoIn)
        giorno = cal.get(Calendar.DAY_OF_YEAR)

        return giorno
    }// fine del metodo

    //--tiene conto anche dell'anno
    public static int getNumGiornoAssoluto(Date giornoIn) {
        int annoGiorno
        int anno
        int giorno
        Calendar cal = Calendar.getInstance()

        cal.setTime(giornoIn)
        anno = cal.get(Calendar.YEAR)
        giorno = cal.get(Calendar.DAY_OF_YEAR)

        annoGiorno = anno * 1000 + giorno
        return annoGiorno
    }// fine del metodo

    public static int getNumGiornoMese(Date giornoIn) {
        int giorno
        Calendar cal = Calendar.getInstance()

        cal.setTime(giornoIn)
        giorno = cal.get(Calendar.DAY_OF_MONTH)

        return giorno
    }// fine del metodo

    public static int getNumGiorniNelMese(Date giornoIn) {
        int giorni
        int mese
        Calendar cal = Calendar.getInstance()

        cal.setTime(giornoIn)
        mese = cal.get(Calendar.MONTH) + 1
        giorni = Mese.getGiorni(mese)

        return giorni
    }// fine del metodo

    // for the current Locale
    public static String getGiornoSettimana() {
        return getGiornoSettimana(creaDataOggi())
    }// fine del metodo

    // for the current Locale
    public static String getGiornoSettimana(Date giorno) {
        String giornoSettimana = ''
        int numSettimana
        Calendar cal = Calendar.getInstance()
        DateFormatSymbols symbols = new DateFormatSymbols()
        String[] dayNames

        dayNames = symbols.getShortWeekdays()

        cal.setTime(giorno)
        numSettimana = cal.get(Calendar.DAY_OF_WEEK)
        giornoSettimana = dayNames[numSettimana]

        return giornoSettimana
    }// fine del metodo


    public static int getNumMese(Date giornoIn) {
        int mese
        Calendar cal = Calendar.getInstance()

        cal.setTime(giornoIn)
        mese = cal.get(Calendar.MONTH) + 1

        return mese
    }// fine del metodo

    public static int getNumOra(Date giornoIn) {
        int ora
        Calendar cal = Calendar.getInstance()

        cal.setTime(giornoIn)
        ora = cal.get(Calendar.HOUR_OF_DAY)

        return ora
    }// fine del metodo

    public static int getNumMinuti(Date giornoIn) {
        int minuti
        Calendar cal = Calendar.getInstance()

        cal.setTime(giornoIn)
        minuti = cal.get(Calendar.MINUTE)

        return minuti
    }// fine del metodo

    public static int getNumSettimana(Date giornoIn) {
        int numSettimana
        Calendar cal = Calendar.getInstance()

        cal.setTime(giornoIn)
        numSettimana = cal.get(Calendar.DAY_OF_WEEK)

        return numSettimana
    }// fine del metodo

    public static int getSettimana(Date giornoIn) {
        int settimana
        Calendar cal = Calendar.getInstance()

        cal.setTime(giornoIn)
        settimana = cal.get(Calendar.WEEK_OF_YEAR)

        return settimana
    }// fine del metodo

    public static int getDurataOre(Date inizio, Date fine) {
        int durata

        durata = getNumOra(inizio) - getNumOra(fine)
        durata = Math.abs(durata)

        return durata
    }// fine del metodo

    public static int getNumSettimanaOdierna() {
        int settimana
        Date oggi = creaDataOggi()

        settimana = getSettimana(oggi)

        return settimana
    }// fine del metodo

    public static int getNumSettimanaTurno(Turno turno) {
        int settimana
        Date giornoTurno = turno.giorno

        settimana = getSettimana(giornoTurno)

        return settimana
    }// fine del metodo

    //--Costruisce il tag controller per il testo indicato
    public static String tagController(String controller, String titolo, String azione) {
        String testoOut = ''

        if (controller && titolo) {
            testoOut += '<li class="controller">'
            testoOut += '<a href="/webambulanze/'
            testoOut += controller
            testoOut += '/'
            if (azione) {
                testoOut += azione
            } else {
                testoOut += 'index'
            }// fine del blocco if-else
            testoOut += '">'
            testoOut += titolo
            testoOut += '</a>'
            testoOut += '</li>'
        }// fine del blocco if

        return testoOut
    } // fine del metodo statico

    //--Costruisce il tag controller per il testo indicato
    public static String tagController(String controller, String titolo) {
        return Lib.tagController(controller, titolo, '')
    } // fine del metodo statico

    //--Costruisce il tag controller per il testo indicato
    public static String tagController(String controller) {
        return Lib.tagController(controller, controller)
    } // fine del metodo statico

    //--Turni odierni necessari
    //--Normalmente 2 turni al mese
    //--Il mese corrente lo si considera ''in corso'' e il conteggio scatta alla fine del mese
    //--Così il 25 febbraio bastano 2 turni, il 30 marzo ne bastano 4 ma il 1° aprile ce ne vogliono 6
    //--le oreExtra (se ci sono) valgono 6-7 ogni turno
    public static int turniNecessari(int oreExtra) {
        int turniNecessari
        Date oggi = new Date()
        int turniMese = 2
        int numMeseCorrente = getNumMese(oggi)
        int oreConvenzionaliPerTurno = 6

        turniNecessari = turniMese * (numMeseCorrente - 1)

        if (oreExtra) {
            turniNecessari -= (oreExtra / oreConvenzionaliPerTurno)
        }// fine del blocco if

        return turniNecessari
    } // fine del metodo statico

    //--Turni odierni necessari
    //--Normalmente 2 turni al mese
    //--Il mese corrente lo si considera ''in corso'' e il conteggio scatta alla fine del mese
    //--Così il 25 febbraio bastano 2 turni, il 30 marzo ne bastano 4 ma il 1° aprile ce ne vogliono 6
    public static int turniNecessari() {
        return turniNecessari(0)
    } // fine del metodo statico

    //--Costruisce il tag controller per il testo indicato
    public static Map cloneMappa(Map mappa) {
        HashMap mappaClonata = null

        if (mappa) {
            mappaClonata = new HashMap()
            mappa.keySet().each {
                mappaClonata.put(it, mappa.get(it))
            } // fine del ciclo each
        }// fine del blocco if

        return mappaClonata
    } // fine del metodo statico

    /**
     * Formattazione di un numero.
     * <p/>
     * Il numero può arrivare come stringa, intero o double
     * Se la stringa contiene punti e virgole, viene pulita
     * Se la stringa non è convertibile in numero, viene restituita uguale
     * Inserisce il punto separatore ogni 3 cifre
     * Se arriva un oggetto non previsto, restituisce l'oggetto

     * @param numero da formattare (stringa, intero o double)
     * @return numero formattato
     */
    public static formatNum(numero) {
        // variabili e costanti locali di lavoro
        def formattato = numero
        String sep = '.'
        int len
        String num3
        String num6
        String num9
        String num12

        if (numero instanceof Integer || numero instanceof Long) {
            numero = (String) numero
        }// fine del blocco if

        // controllo di congruità
        if (numero instanceof String || numero instanceof Integer || numero instanceof Double || numero instanceof BigDecimal) {
            formattato = ''

            if (numero instanceof String) {
                numero = levaTesto(numero, ',')
                numero = levaTesto(numero, '.')
                formattato = numero

                try { // prova ad eseguire il codice
                    Integer.decode(numero)
                    len = numero.length()
                    if (len > 3) {
                        num3 = numero.substring(0, len - 3)
                        num3 += sep
                        num3 += numero.substring(len - 3)
                        formattato = num3
                        if (len > 6) {
                            num6 = num3.substring(0, len - 6)
                            num6 += sep
                            num6 += num3.substring(len - 6)
                            formattato = num6
                            if (len > 9) {
                                num9 = num6.substring(0, len - 9)
                                num9 += sep
                                num9 += num6.substring(len - 9)
                                formattato = num9
                                if (len > 12) {
                                    num12 = num9.substring(0, len - 12)
                                    num12 += sep
                                    num12 += num9.substring(len - 12)
                                    formattato = num12
                                }// fine del blocco if
                            }// fine del blocco if
                        }// fine del blocco if
                    }// fine del blocco if
                } catch (Exception unErrore) { // intercetta l'errore
                }// fine del blocco try-catch
            }// fine del blocco if

            if (numero instanceof Integer) {
                formattato = formatNum(numero.toString())
            }// fine del blocco if

            if (numero instanceof Double) {
                formattato = formatNum(numero.toString())
            }// fine del blocco if

            if (numero instanceof BigDecimal) {
                formattato = formatNum(numero.toString())
            }// fine del blocco if

        }// fine del blocco if

        // valore di ritorno
        return formattato
    } // fine del metodo

    /**
     * Elimina tutti i caratteri contenuti nella stringa.
     * Esegue solo se il testo è valido
     *
     * @param testoIn in ingresso
     * @param subStringa da eliminare
     * @return testoOut stringa convertita
     */
    public static levaTesto(def testoIn, String subStringa) {
        // variabili e costanti locali di lavoro
        def testoOut = testoIn

        if (testoIn && testoIn instanceof String) {
            testoOut = testoIn.trim()
            if (testoOut.contains(subStringa)) {
                testoOut = sostituisce(testoOut, subStringa, '')
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return testoOut
    } // fine del metodo

    /**
     * Sostituisce tutte le occorrenze.
     * Esegue solo se il testo è valido
     * Se arriva un oggetto non stringa, restituisce l'oggetto
     *
     * @param testoIn in ingresso
     * @param oldStringa da eliminare
     * @param newStringa da sostituire
     * @return testoOut convertito
     */
    public static sostituisce(def testoIn, String oldStringa, String newStringa) {
        // variabili e costanti locali di lavoro
        def testoOut = testoIn
        int pos = 0
        String prima = ''

        if (testoIn && testoIn instanceof String && oldStringa && newStringa instanceof String) {
            testoOut = testoIn.trim()
            if (testoIn.contains(oldStringa)) {

                while (pos != INT_NULLO) {
                    pos = testoIn.indexOf(oldStringa)
                    if (pos != INT_NULLO) {
                        prima += testoIn.substring(0, pos)
                        prima += newStringa
                        pos += oldStringa.length()
                        testoIn = testoIn.substring(pos)
                    }// fine del blocco if
                }// fine di while

                testoOut = prima + testoIn
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return testoOut
    } // fine del metodo

    //--controlla se il giorno della settimana è quello richiesto
    //--i giorni della settimana partono da domenica
    //--domenica=1
    public static boolean isGiornoSettimana(Date giorno, int numSettimana) {
        boolean giornoSettimana = false
        int settimana = getNumSettimana(giorno)

        if (settimana == numSettimana) {
            giornoSettimana = true
        }// fine del blocco if

        return giornoSettimana
    } // fine del metodo statico

    //--controlla se il giorno è domenica (1)
    public static boolean isDomenica(Date giorno) {
        return isGiornoSettimana(giorno, 1)
    } // fine del metodo statico

    //--controlla se il giorno è lunedi (2)
    public static boolean isLunedi(Date giorno) {
        return isGiornoSettimana(giorno, 2)
    } // fine del metodo statico

    //--controlla se il giorno è martedi (3)
    public static boolean isMartedi(Date giorno) {
        return isGiornoSettimana(giorno, 3)
    } // fine del metodo statico

    //--controlla se il giorno è mercoledi (4)
    public static boolean isMercoledi(Date giorno) {
        return isGiornoSettimana(giorno, 4)
    } // fine del metodo statico

    //--controlla se il giorno è giovedi (5)
    public static boolean isGiovedi(Date giorno) {
        return isGiornoSettimana(giorno, 5)
    } // fine del metodo statico

    //--controlla se il giorno è venerdi (6)
    public static boolean isVenerdi(Date giorno) {
        return isGiornoSettimana(giorno, 6)
    } // fine del metodo statico

    //--controlla se il giorno è sabato (7)
    public static boolean isSabato(Date giorno) {
        return isGiornoSettimana(giorno, 7)
    } // fine del metodo statico

    //--controlla se il giorno è feriale
    //--si intendono i giorni compresi tra lunedi e venerdi
    //--lunedì=2 e venerdi=6
    public static boolean isFeriale(Date giorno) {
        boolean feriale = false
        def feriali = [2, 3, 4, 5, 6]
        int settimana = getNumSettimana(giorno)

        //-- lunedì(2) e sabato(7)
        if (settimana in feriali) {
            feriale = true
        }// fine del blocco if

        return feriale
    } // fine del metodo statico

    //--controlla se il giorno è feriale
    //--si intendono i giorni compresi tra lunedi e venerdi
    //--comunque non festivo
    public static boolean isFerialeAnno(Date giorno, ArrayList festivi) {
        boolean feriale = false
        int progressivo = getNumGiorno(giorno)

        if (isFeriale(giorno) && !(progressivo in festivi)) {
            feriale = true
        }// fine del blocco if

        return feriale
    } // fine del metodo statico

    //--controlla se il giorno è feriale
    //--si intendono i giorni compresi tra lunedi e venerdi
    //--comunque non festivo per l'anno indicato
    public static boolean isFerialeAnno(Date giorno, String anno) {
        return isFerialeAnno(giorno, Festivi.all(anno))
    } // fine del metodo statico

    //--controlla se il giorno è festivo
    //--si intendono i giorni di sabato e domenica
    //--sabato=7 e domenica=1
    public static boolean isFestivo(Date giorno) {
        boolean festivo = false
        def festivi = [1, 7]
        int settimana = getNumSettimana(giorno)

        //--sabato(7) e domenica(1)
        if (settimana in festivi) {
            festivo = true
        }// fine del blocco if

        return festivo
    } // fine del metodo statico

    //--controlla se il giorno è sabato, domenica o comunque festivo
    public static boolean isFestivoAnno(Date giorno, ArrayList festivi) {
        boolean festivo = false
        int progressivo = getNumGiorno(giorno)

        if (isFestivo(giorno) || (progressivo in festivi)) {
            festivo = true
        }// fine del blocco if

        return festivo
    } // fine del metodo statico

    //--controlla se il giorno è sabato, domenica o comunque festivo per l'anno indicato
    public static boolean isFestivoAnno(Date giorno, String anno) {
        return isFestivoAnno(giorno, Festivi.all(anno))
    } // fine del metodo statico

    //--controlla se il giorno è prefestivo
    //--si intendono i giorni di venerdi e sabato
    //--venerdi=6 e sabato=7
    public static boolean isPreFestivo(Date giorno) {
        return isFestivo(giorno + 1)
    } // fine del metodo statico

    //--controlla se il giorno è venerdi, sabato o comunque prefestivo
    public static boolean isPreFestivoAnno(Date giorno, ArrayList festivi) {
        boolean festivo = false
        int progressivo = getNumGiorno(giorno) + 1

        if (isPreFestivo(giorno) || (progressivo in festivi)) {
            festivo = true
        }// fine del blocco if

        return festivo
    } // fine del metodo statico

    //--controlla se il giorno è venerdì, sabato o comunque prefestivo per l'anno indicato
    public static boolean isPreFestivoAnno(Date giorno, String anno) {
        return isPreFestivoAnno(giorno, Festivi.all(anno))
    } // fine del metodo statico

    /**
     * Presenta la data
     */
    public static String getGiornoTxt(Date giorno) {
        String giornoTxt = ''
        String sep = ','
        String spazio = ' '

        giornoTxt += getGiorno(giorno)
        giornoTxt += sep
        giornoTxt += spazio
        giornoTxt += getNumGiornoMese(giorno)
        giornoTxt += spazio
        giornoTxt += getMese(giorno)
        giornoTxt += spazio
        giornoTxt += getAnno(giorno)

        return giornoTxt
    }// fine del metodo

} // fine della classe
