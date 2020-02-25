package webambulanze

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder

import javax.servlet.http.Cookie

class AccessoController {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def springSecurityService

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def rememberMeServices

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def croceService

    //--sigla della croce corrente
    public static String SIGLA_CROCE = 'nessuna'

    //--controller che gestisce il tabellone
    public static String START_CONTROLLER = 'turno'

    //--tabellone turni
    @Secured([Cost.ROLE_MILITE])
    def index() {
        redirect(controller: 'turno', action: 'tabellone')
    } // fine del metodo

    //--chiamata dai menu delle liste e form
    //--va al menu base
    def home() {
        params.siglaCroce = croceService.getSiglaCroce(request)
        render(controller: 'accesso', view: 'home', params: params)
    } // fine del metodo

    def help() {
        render(view: '/help')
    } // fine del metodo

    def seleziona() {
        String possibileSiglaCroce = ''
        Croce croce = null
        String siglaCroce = ''
        String spazioVuoto = ' '
        boolean primaVolta
        boolean mostraTabellonePartenza = false
        boolean isTabelloneSecured = true
        String tag = "gac"
        boolean developer = false

        def abc = params
        if (params.siglaCroce) {
            possibileSiglaCroce = params.siglaCroce

            if (possibileSiglaCroce.startsWith(tag)) {
                possibileSiglaCroce = possibileSiglaCroce.substring(tag.length())
                developer = true
            }// end of if cycle

            if (possibileSiglaCroce.contains(spazioVuoto)) {
                possibileSiglaCroce = possibileSiglaCroce.substring(0, possibileSiglaCroce.indexOf(spazioVuoto))
            }// fine del blocco if
            croce = Croce.findBySigla(possibileSiglaCroce)
            if (croce) {
                siglaCroce = croce.sigla
            } else {
                flash.errors = "La sigla $possibileSiglaCroce, non rappresenta un'associazione valida"
                render(view: '/error')
                return
            }// fine del blocco if-else
        } else {
            render(view: '/error')
            return
        }// fine del blocco if-else

        if (!developer && siglaCroce.equalsIgnoreCase("gaps")) {
            flash.errors = "La croce $possibileSiglaCroce, è stata spostata. Usa 'gaps.algos.biz'";
            render(view: '/error')
            return
        }// end of if cycle

//        if (!developer && siglaCroce.equalsIgnoreCase("crf")) {
//            flash.errors = "La croce $possibileSiglaCroce, è stata spostata. Usa 'crf.algos.biz'";
//            render(view: '/error')
//            return
//        }// end of if cycle

//        if (!developer && siglaCroce.equalsIgnoreCase("crpt")) {
//            flash.errors = "La croce $possibileSiglaCroce, è stata spostata. Usa 'crpt.algos.biz'";
//            render(view: '/error')
//            return
//        }// end of if cycle

//        if (!developer && siglaCroce.equalsIgnoreCase("pap")) {
//            flash.errors = "La croce $possibileSiglaCroce, è stata spostata. Usa 'pap.algos.biz'";
//            render(view: '/error')
//            return
//        }// end of if cycle

        //--regolazioni generali
        primaVolta = selezionaCroceBase(siglaCroce)

        primaVolta = false

        if (croceService) {
            mostraTabellonePartenza = croceService.mostraTabellonePartenza(siglaCroce)
            isTabelloneSecured = croceService.isTabelloneSecured(siglaCroce)
        }// fine del blocco if

        if (siglaCroce.equals(Cost.CROCE_DEMO)) {
            //--deve esserci un utente (altrimenti non dovrebbe arrivare qui, ma per sicurezza...)
        }// fine del blocco if

        if (primaVolta) {
            redirect(url: '/' + siglaCroce)
        } else {
            if (mostraTabellonePartenza) {
                //--va alla schermata specifica
                if (isTabelloneSecured) {
                    redirect(controller: START_CONTROLLER, action: 'indexSecured', params: params)
                } else {
                    redirect(controller: START_CONTROLLER, action: 'index', params: params)
                }// fine del blocco if-else
            } else {
                //--va al menu base
                render(controller: 'accesso', view: 'home', params: params)
            }// fine del blocco if-else
        }// fine del blocco if-else
    } // fine del metodo

//    public  void reauthenticate() {
//        final String username = 'ospite'
//        final String password = 'ospite'
//        ApplicationContext ctx = grailsApplication.mainContext
//        UserDetailsService userDetailsService = ctx.getBean("userDetailsService");
//        UserCache userCache = ctx.getBean("userCache");
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
//                userDetails, password == null ? userDetails.getPassword() : password, userDetails.getAuthorities()));
//        userCache.removeUserFromCache(username);
//    }

    def selezionaLogin = {
//        Croce croce
//        String siglaCroce
//
//        //--recupera la croce
//        if (croceService) {
//            croce = croceService.getCroce(request)
//        }// fine del blocco if
//        if (croce) {
//            siglaCroce = croce.sigla
//        } else {
//            render(view: '/error')
//            return
//        }// fine del blocco if-else
//         def a=params
        redirect(controller: START_CONTROLLER, action: 'indexSecured', params: params)
    } // fine del metodo

    //--selezione iniziale della croce su cui operare
    //--seleziona la necessità del login
    //--regola la schermata iniziale
    def boolean selezionaCroceBase(String siglaCroce) {
        String oldCookie

        //--pulizia
        SecurityContextHolder.clearContext()
        rememberMeServices.logout request, response, null

        //--cancella il cookie di Security
        if (false) {
            Cookie cookie = new Cookie('JSESSIONID', '')
            cookie.maxAge = 10
            response.addCookie(cookie)
        }// fine del blocco if

        //--eventuale cookie già esistente per la stessa croce
        oldCookie = g.cookie(name: Cost.COOKIE_SIGLA_CROCE)

        //--selezione iniziale della croce su cui operare
        if (oldCookie && oldCookie.equals(siglaCroce)) {
            //non fa nulla
            return false
        } else {
            Cookie cookie = new Cookie(Cost.COOKIE_SIGLA_CROCE, siglaCroce)
            cookie.maxAge = 100000
            response.addCookie(cookie)
            return true
        }// fine del blocco if-else
    } // fine del metodo

} // fine della controller classe
