package webambulanze

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.dao.DataIntegrityViolationException


//@Secured([Cost.ROLE_CUSTODE])
class UtenteController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def utenteService

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def logoService

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def croceService

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def militeService

    def index() {
        redirect(action: 'list', params: params)
    } // fine del metodo

    def list(Integer max) {
        def lista
        Croce croce = croceService.getCroce(request)
        def campiLista = [
                'nickname',
                'pass',
                'milite',
                'enabled',
                'accountExpired',
                'accountLocked',
                'passwordExpired']

        if (params.order) {
            if (params.order == 'asc') {
                params.order = 'desc'
            } else {
                params.order = 'asc'
            }// fine del blocco if-else
        } else {
            params.order = 'asc'
        }// fine del blocco if-else

        if (croce) {
            params.siglaCroce = croce.sigla
            if (params.siglaCroce.equals(Cost.CROCE_ALGOS)) {
                lista = Utente.findAll("from Utente order by croce_id,username")
                campiLista = ['id', 'croce'] + campiLista
            } else {
                if (!params.sort) {
                    params.sort = 'username'
                }// fine del blocco if-else
                lista = utenteService.tuttiQuelliDellaCroceSenzaProgrammatore(croce, params)
            }// fine del blocco if-else
        } else {
            lista = utenteService.tuttiSenzaProgrammatore(params)
        }// fine del blocco if-else

        render(view: 'list', model: [utenteInstanceList: lista, utenteInstanceTotal: 0, campiLista: campiLista], params: params)
    } // fine del metodo

    //--ATTENZIONE - se si ricreano le viste, occorre modificare  -form.gsp
    //--mettendo from="${lista}" nel primo campo
    def create() {
        def lista = null
        Croce croce = croceService.getCroce(request)

        if (!params.sort) {
            params.sort = 'username'
        }// fine del blocco if

        if (croce) {
            params.siglaCroce = croce.sigla
            lista = militeService.allMilitiDellaCroce(croce)
        }// fine del blocco if

        render(view: 'create', model: [utenteInstance: new Utente(params), lista: lista], params: params)
    } // fine del metodo

    def save() {
        Croce croce = croceService.getCroce(request)
        def utenteInstance = new Utente(params)

        if (croce) {
            params.siglaCroce = croce.sigla
            utenteInstance.croce = croce
        }// fine del blocco if

        utenteInstance.username = utenteInstance.nickname + '/' + croce.sigla.toLowerCase()

        if (!utenteInstance.save(flush: true)) {
            render(view: 'create', model: [utenteInstance: utenteInstance], params: params)
            return
        }// fine del blocco if

        if (utenteInstance.milite) {
            flash.message = logoService.setWarn(Evento.utenteCreato, utenteInstance.milite)
        }// fine del blocco if

        Ruolo ruoloMilite = Ruolo.findByAuthority(Cost.ROLE_MILITE)
        UtenteRuolo.create(utenteInstance, ruoloMilite, true)

        flash.message = message(code: 'default.created.message', args: [message(code: 'utente.label', default: 'Utente'), utenteInstance.id])
        redirect(action: 'show', id: utenteInstance.id)
    } // fine del metodo

    def show(Long id) {
        params.siglaCroce = croceService.getSiglaCroce(request)

        def utenteInstance = Utente.get(id)

        if (!utenteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'utente.label', default: 'Utente'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if

        render(view: 'show', model: [utenteInstance: utenteInstance], params: params)
    } // fine del metodo

    //--ATTENZIONE - se si ricreano le viste, occorre modificare  -form.gsp
    //--mettendo from="${lista}" nel primo campo
    def edit(Long id) {
        Croce croce = croceService.getCroce(request)
        def lista = null

        def utenteInstance = Utente.get(id)

        if (!utenteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'utente.label', default: 'Utente'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if

        if (croce) {
            params.siglaCroce = croce.sigla
            lista = militeService.allMilitiDellaCroce(croce)
        }// fine del blocco if

        render(view: 'edit', model: [utenteInstance: utenteInstance, lista: lista], params: params)
    } // fine del metodo

    def update(Long id, Long version) {
        params.siglaCroce = croceService.getSiglaCroce(request)

        def utenteInstance = Utente.get(id)

        if (!utenteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'utente.label', default: 'Utente'), id])
            redirect(action: "list")
            return
        }// fine del blocco if

        if (version != null) {
            if (utenteInstance.version > version) {
                utenteInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'utente.label', default: 'Utente')] as Object[],
                        "Another user has updated this Utente while you were editing")
                render(view: 'edit', model: [utenteInstance: utenteInstance], params: params)
                return
            }// fine del blocco if
        }// fine del blocco if

        flash.listaMessaggi = utenteService.avvisoModifiche(params, utenteInstance)
        utenteInstance.properties = params

        if (!utenteInstance.save(flush: true)) {
            render(view: 'edit', model: [utenteInstance: utenteInstance], params: params)
            return
        }// fine del blocco if

        redirect(action: 'show', id: utenteInstance.id)
    } // fine del metodo

    @Secured([Cost.ROLE_PROG])
    def delete(Long id) {
        def utenteInstance = Utente.get(id)

        if (!utenteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'utente.label', default: 'Utente'), id])
            redirect(action: "list")
            return
        }// fine del blocco if

        try {
            utenteInstance.delete()
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'utente.label', default: 'Utente'), id])
            redirect(action: 'list')
        }
        catch (DataIntegrityViolationException e) {
            try { // prova ad eseguire il codice
                Logo logo
                def listaLogo = Logo.findAllByUtente(utenteInstance)
                listaLogo?.each {
                    logo = (Logo) it
                    logo.delete(flush: true)
                } // fine del ciclo each

//                String query = "delete utente_ruolo where utente_id=" + utenteInstance.id
//                UtenteRuolo.executeUpdate(query)

                def listaUtenteRuoli = UtenteRuolo.findAllByUtente(utenteInstance)
                listaUtenteRuoli?.each {
                    it.delete(flush: true)
                } // fine del ciclo each

//                Ruolo ruolo = Ruolo.get(4)
//                UtenteRuolo.remove utenteInstance, ruolo
//                utenteInstance.delete()
            } catch (Exception unErrore) { // intercetta l'errore
                log.error unErrore
            }// fine del blocco try-catch
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'utente.label', default: 'Utente'), id])
            redirect(action: 'show', id: id)
        }
    } // fine del metodo


    public static int deleteLink(Milite milite) {
        int status = 0 //indeterminato
        Utente istanza = Utente.findByMilite(milite)

        if (istanza == null) {
            status = 1 //non esiste
        } else {
            status = 2 //non riesco a cancellarlo
            try {
                istanza.delete(flush: true)
                status = 3 //cancellato
            } catch (DataIntegrityViolationException e) {
            }// fine del blocco try-catch
        }// end of if/else cycle

        return status
    } // fine del metodo

} // fine della controller classe
