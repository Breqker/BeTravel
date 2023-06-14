class Utente private constructor(val id: Int, val nome: String, val cognome: String) {
    companion object {
        private var utente: Utente? = null

        fun getUtente(): Utente? {
            return utente
        }

        fun creaUtenteStaticoPersonalizzato(id: Int, nome: String, cognome: String) {
            utente = Utente(id, nome, cognome)
        }

        fun getId(): Int? {
            return utente?.id
        }
    }
}
