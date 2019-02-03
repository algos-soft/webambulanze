/* Created by Algos s.r.l. */
/* Date: mag 2012 */
/* Questo file è stato installato dal plugin AlgosBase */
/* Tipicamente NON verrà più sovrascritto dalle successive release del plugin */
/* in quanto POTREBBE essere personalizzato in questa applicazione */
/* Se vuoi che le prossime release del plugin sovrascrivano questo file, */
/* perdendo tutte le modifiche effettuate qui, */
/* regola a true il flag di controllo flagOverwrite© */
/* flagOverwrite = false */

package webambulanze

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException

@Secured([Cost.ROLE_MILITE])
@Transactional(readOnly = false)
class MilitestatisticheController {
    static boolean transactional = false

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    private static String PREFIX = 'anno'

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def militeService
    def funzioneService
    def militeturnoService
    def croceService
    def utenteService

    private static String ANNO_CORRENTE = '2019'
    private String anno = ANNO_CORRENTE

    def index() {
        redirect(action: 'list', params: params)
    } // fine del metodo

    def anno2012() {
        params.anno = '2012'
        redirect(action: 'list', params: params)
    } // fine del metodo

    def anno2013() {
        params.anno = '2013'
        redirect(action: 'list', params: params)
    } // fine del metodo

    def anno2014() {
        params.anno = '2014'
        redirect(action: 'list', params: params)
    } // fine del metodo

    def anno2015() {
        params.anno = '2015'
        redirect(action: 'list', params: params)
    } // fine del metodo

    def anno2016() {
        params.anno = '2016'
        redirect(action: 'list', params: params)
    } // fine del metodo

    def anno2017() {
        params.anno = '2017'
        redirect(action: 'list', params: params)
    } // fine del metodo

    def anno2018() {
        params.anno = '2018'
        redirect(action: 'list', params: params)
    } // fine del metodo

    def anno2019() {
        params.anno = '2019'
        redirect(action: 'list', params: params)
    } // fine del metodo

    def list(int max) {
        def lista = null
        Croce croce = croceService.getCroce(request)
        Milite milite
        ArrayList menuExtra
        HashMap mappa = new HashMap()
        String titoloLista = "Turni effettuati dai militi nell'anno 2019"
        mappa.put('titolo', 'nomignolo')
        mappa.put('campo', 'database')

        //--selezione dei menu extra
        //--solo azione e di default controller=questo; classe e titolo vengono uguali
        //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
        menuExtra = new ArrayList()
        Cost.ANNI?.each {
            menuExtra.add(PREFIX + it)
        } // fine del ciclo each

        // fine della definizione

        def campiLista = [[:],
                          'milite',
                          'ultimo',
                          'delta',
                          'status',
                          'turni',
                          'ore',
                          'oreExtra']
        def campiExtra

        if (params.anno) {
            anno = params.anno
        }// fine del blocco if
        if (params.order) {
            if (params.order == 'asc') {
                params.order = 'desc'
            } else {
                params.order = 'asc'
            }// fine del blocco if-else
        } else {
            params.order = 'asc'
        }// fine del blocco if-else
        if (params.sort.equals('milite')) {
            params.sort = 'milite.cognome'
        }// fine del blocco if

        //--modifica del titolo in funzione dell'anno
        if (anno) {
            titoloLista = "Turni effettuati dai militi nell'anno ${anno}"
        }// fine del blocco if

        if (croce) {
            params.siglaCroce = croce.sigla
            if (params.siglaCroce.equals(Cost.CROCE_ALGOS)) {
                lista = Militestatistiche.findAll("from Militestatistiche where anno=2013 order by croce_id,milite_id")
                campiLista = ['id', 'croce'] + campiLista
            } else {
                if (!params.sort) {
                    params.sort = 'milite'
                }// fine del blocco if-else
                if (militeService.isLoggatoAdminOrMore()) {
                    lista = Militestatistiche.findAllByCroceAndAnno(croce, anno, params)
                } else {
                    milite = militeService.militeLoggato
                    if (milite) {
                        lista = Militestatistiche.findByMiliteAndAnno(milite, anno)
                    }// fine del blocco if
                }// fine del blocco if-else
                campiExtra = funzioneService.campiExtraPerCroce(croce)
                if (campiExtra) {
                    for (int k = 1; k <= campiExtra.size(); k++) {
                        mappa = ['title': campiExtra.get(k - 1), 'campo': 'funz' + "${k}"]
                        campiLista.add(mappa)
                    } // fine del ciclo for
                }// fine del blocco if
            }// fine del blocco if-else
        } else {
            lista = Milite.findAll(params)
        }// fine del blocco if-else

        //--elimina il primo elemento della lista che serviva solo per evitare che fosse una lista di stringhe
        campiLista.remove([:])

        render(view: 'list', model: [
                militestatisticheInstanceList : lista,
                militestatisticheInstanceTotal: 0,
                menuExtra                     : menuExtra,
                titoloLista                   : titoloLista,
                campiLista                    : campiLista,
                campiExtra                    : null],
                params: params)
    } // fine del metodo

    def calcola() {
        militeturnoService.calcola(request)
        utenteService.regolaAbilitazioni()
        redirect(action: 'list', params: params)
    } // fine del metodo


    def cancella2013() {
        Croce croce = croceService.getCroce(request)
        String anno = Cost.ANNI[1]
        Date inizio
        Date fine

        if (croce) {
            inizio = Lib.creaData1Gennaio(anno)
            fine = Lib.creaData31Dicembre(anno)
            militeturnoService.cancellaMiliteTurno(croce, inizio, fine)
        }// fine del blocco if
        redirect(action: 'list', params: params)
    } // fine del metodo

    def calcola2013() {
        Croce croce = croceService.getCroce(request)
        String anno = Cost.ANNI[1]
        Date inizio
        Date fine

        if (croce) {
            inizio = Lib.creaData1Gennaio(anno)
            fine = Lib.creaData31Dicembre(anno)
            militeturnoService.calcola(croce, anno, inizio, fine)
        }// fine del blocco if
        utenteService.regolaAbilitazioni()
        redirect(action: 'list', params: params)
    } // fine del metodo

    def cancella2014() {
        Croce croce = croceService.getCroce(request)
        String anno = Cost.ANNI[2]
        Date inizio
        Date fine

        if (croce) {
            inizio = Lib.creaData1Gennaio(anno)
            fine = Lib.creaData31Dicembre(anno)
            militeturnoService.cancellaMiliteTurno(croce, inizio, fine)
        }// fine del blocco if
        redirect(action: 'list', params: params)
    } // fine del metodo

    def calcola2014() {
        Croce croce = croceService.getCroce(request)
        String anno = Cost.ANNI[2]
        Date inizio
        Date fine

        if (croce) {
            inizio = Lib.creaData1Gennaio(anno)
            fine = Lib.creaData31Dicembre(anno)
            militeturnoService.calcola(croce, anno, inizio, fine)
        }// fine del blocco if
        utenteService.regolaAbilitazioni()
        redirect(action: 'list', params: params)
    } // fine del metodo

    def cancella2015() {
        Croce croce = croceService.getCroce(request)
        String anno = Cost.ANNI[3]
        Date inizio
        Date fine

        if (croce) {
            inizio = Lib.creaData1Gennaio(anno)
            fine = Lib.creaData31Dicembre(anno)
            militeturnoService.cancellaMiliteTurno(croce, inizio, fine)
        }// fine del blocco if
        redirect(action: 'list', params: params)
    } // fine del metodo

    def calcola2015() {
        Croce croce = croceService.getCroce(request)
        String anno = Cost.ANNI[3]
        Date inizio
        Date fine

        if (croce) {
            inizio = Lib.creaData1Gennaio(anno)
            fine = Lib.creaDataOggi()
            militeturnoService.calcola(croce, anno, inizio, fine)
        }// fine del blocco if
        utenteService.regolaAbilitazioni()
        redirect(action: 'list', params: params)
    } // fine del metodo

    def cancella2016() {
        Croce croce = croceService.getCroce(request)
        String anno = Cost.ANNI[4]
        Date inizio
        Date fine

        if (croce) {
            inizio = Lib.creaData1Gennaio(anno)
            fine = Lib.creaData31Dicembre(anno)
            militeturnoService.cancellaMiliteTurno(croce, inizio, fine)
        }// fine del blocco if
        redirect(action: 'list', params: params)
    } // fine del metodo

    def calcola2016() {
        Croce croce = croceService.getCroce(request)
        String anno = Cost.ANNI[4]
        Date inizio
        Date fine

        if (croce) {
            inizio = Lib.creaData1Gennaio(anno)
            fine = Lib.creaDataOggi()
            militeturnoService.calcola(croce, anno, inizio, fine)
        }// fine del blocco if
        utenteService.regolaAbilitazioni()
        redirect(action: 'list', params: params)
    } // fine del metodo

    def cancella2017() {
        Croce croce = croceService.getCroce(request)
        String anno = Cost.ANNI[5]
        Date inizio
        Date fine

        if (croce) {
            inizio = Lib.creaData1Gennaio(anno)
            fine = Lib.creaData31Dicembre(anno)
            militeturnoService.cancellaMiliteTurno(croce, inizio, fine)
        }// fine del blocco if
        redirect(action: 'list', params: params)
    } // fine del metodo

    def calcola2017() {
        Croce croce = croceService.getCroce(request)
        String anno = Cost.ANNI[5]
        Date inizio
        Date fine

        if (croce) {
            inizio = Lib.creaData1Gennaio(anno)
            fine = Lib.creaDataOggi()
            militeturnoService.calcola(croce, anno, inizio, fine)
        }// fine del blocco if
        utenteService.regolaAbilitazioni()
        redirect(action: 'list', params: params)
    } // fine del metodo

    def cancella2018() {
        Croce croce = croceService.getCroce(request)
        String anno = Cost.ANNI[6]
        Date inizio
        Date fine

        if (croce) {
            inizio = Lib.creaData1Gennaio(anno)
            fine = Lib.creaData31Dicembre(anno)
            militeturnoService.cancellaMiliteTurno(croce, inizio, fine)
        }// fine del blocco if
        redirect(action: 'list', params: params)
    } // fine del metodo

    def calcola2018() {
        Croce croce = croceService.getCroce(request)
        String anno = Cost.ANNI[6]
        Date inizio
        Date fine

        if (croce) {
            inizio = Lib.creaData1Gennaio(anno)
            fine = Lib.creaDataOggi()
            militeturnoService.calcola(croce, anno, inizio, fine)
        }// fine del blocco if
        utenteService.regolaAbilitazioni()
        redirect(action: 'list', params: params)
    } // fine del metodo

    def cancella2019() {
        Croce croce = croceService.getCroce(request)
        String anno = Cost.ANNI[7]
        Date inizio
        Date fine

        if (croce) {
            inizio = Lib.creaData1Gennaio(anno)
            fine = Lib.creaData31Dicembre(anno)
            militeturnoService.cancellaMiliteTurno(croce, inizio, fine)
        }// fine del blocco if
        redirect(action: 'list', params: params)
    } // fine del metodo

    def calcola2019() {
        Croce croce = croceService.getCroce(request)
        String anno = Cost.ANNI[7]
        Date inizio
        Date fine

        if (croce) {
            inizio = Lib.creaData1Gennaio(anno)
            fine = Lib.creaDataOggi()
            militeturnoService.calcola(croce, anno, inizio, fine)
        }// fine del blocco if
        utenteService.regolaAbilitazioni()
        redirect(action: 'list', params: params)
    } // fine del metodo


    @Secured([Cost.ROLE_PROG])
    def create() {
        params.siglaCroce = croceService.getSiglaCroce(request)

        render(view: 'create', model: [militestatisticheInstance: new Militestatistiche(params)], params: params)
    } // fine del metodo

    @Secured([Cost.ROLE_PROG])
    def save() {
        Croce croce = croceService.getCroce(request)
        def militestatisticheInstance = new Militestatistiche(params)

        if (croce) {
            params.siglaCroce = croce.sigla
            if (!militestatisticheInstance.croce) {
                militestatisticheInstance.croce = croce
            }// fine del blocco if
        }// fine del blocco if

        if (!militestatisticheInstance.save(flush: true)) {
            render(view: 'create', model: [militestatisticheInstance: militestatisticheInstance], params: params)
            return
        }// fine del blocco if

        flash.message = message(code: 'default.created.message', args: [message(code: 'militestatistiche.label', default: 'Militestatistiche'), militestatisticheInstance.id])
        redirect(action: 'show', id: militestatisticheInstance.id)
    } // fine del metodo

    @Secured([Cost.ROLE_PROG])
    def show(Long id) {
        def militestatisticheInstance = Militestatistiche.get(id)

        if (!militestatisticheInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'militestatistiche.label', default: 'Militestatistiche'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if

        params.siglaCroce = croceService.getSiglaCroce(request)
        render(view: 'show', model: [militestatisticheInstance: militestatisticheInstance], params: params)
    } // fine del metodo

    @Secured([Cost.ROLE_PROG])
    def edit(Long id) {
        def militestatisticheInstance = Militestatistiche.get(id)

        if (!militestatisticheInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'militestatistiche.label', default: 'Militestatistiche'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if

        params.siglaCroce = croceService.getSiglaCroce(request)
        render(view: 'edit', model: [militestatisticheInstance: militestatisticheInstance], params: params)
    } // fine del metodo

    @Secured([Cost.ROLE_PROG])
    def update(Long id, Long version) {
        def militestatisticheInstance = Militestatistiche.get(id)

        if (!militestatisticheInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'militestatistiche.label', default: 'Militestatistiche'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if

        params.siglaCroce = croceService.getSiglaCroce(request)
        if (version != null) {
            if (militestatisticheInstance.version > version) {
                militestatisticheInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'militestatistiche.label', default: 'Militestatistiche')] as Object[],
                        "Another user has updated this Militestatistiche while you were editing")
                render(view: 'edit', model: [militestatisticheInstance: militestatisticheInstance], params: params)
                return
            }
        }

        militestatisticheInstance.properties = params

        if (!militestatisticheInstance.save(flush: true)) {
            render(view: 'edit', model: [militestatisticheInstance: militestatisticheInstance], params: params)
            return
        }// fine del blocco if

        flash.message = message(code: 'default.updated.message', args: [message(code: 'militestatistiche.label', default: 'Militestatistiche'), militestatisticheInstance.id])
        redirect(action: 'show', id: militestatisticheInstance.id)
    } // fine del metodo

    @Secured([Cost.ROLE_PROG])
    def delete(Long id) {
        def militestatisticheInstance = Militestatistiche.get(id)

        if (!militestatisticheInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'militestatistiche.label', default: 'Militestatistiche'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if

        try {
            militestatisticheInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'militestatistiche.label', default: 'Militestatistiche'), id])
            redirect(action: 'list')
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'militestatistiche.label', default: 'Militestatistiche'), id])
            redirect(action: 'show', id: id)
        }
    } // fine del metodo

    public static int deleteLink(Milite milite) {
        int status = 0 //indeterminato
        def listaStatistiche  = Militestatistiche.findAllByMilite(milite)

        if (listaStatistiche.isEmpty()) {
            status = 1 //non esiste
        } else {
            status = 2 //non riesco a cancellarlo
            for (Militestatistiche istanza : listaStatistiche) {
                try {
                    istanza.delete(flush: true)
                    status = 3 //cancellato
                } catch (DataIntegrityViolationException e) {
                }// fine del blocco try-catch
            }// end of for cycle
        }// end of if/else cycle

        return status
    } // fine del metodo

} // fine della controller classe
