package it.polito.tdp.meteo.model;

import java.util.*;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {

	private List<List<Rilevamento>> meteo;
	private MeteoDAO dao;
	private List<Rilevamento> bestSoluzione = null;
	private double bestCosto;

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	public Model() {

	}

	public List<Rilevamento> getAllRilevamenti() {
		dao = new MeteoDAO();
		return dao.getAllRilevamenti();
	}

	public String getAllRilevamentiLocalitaMese(int mese, String localita) {
		dao = new MeteoDAO();
		return dao.getAllRilevamentiLocalitaMese(mese, localita);
	}

	public List<String> getLocalita() {
		dao = new MeteoDAO();
		return dao.getLocalita();
	}

	/**
	 * RICHIAMO RICORSIONE qui inizializzo tutto quello che serve nella ricorsione
	 * 
	 * @param mese
	 * @return lista di rilevamenti con la sequenza ottima
	 */
	public List<Rilevamento> calcolaSottoinsiemeRilevamenti(int mese) {

		bestCosto = 0.0;
		meteo = new ArrayList<>();

		for (int i = 1; i <= 15; i++) {
			List<Rilevamento> l = new ArrayList<>(MeteoDAO.getAllRilevamentiMeseGiorno(mese, i));
			meteo.add(l);
		}

		List<Rilevamento> parziale = new ArrayList<>();
		trovaSequenza(parziale, 0);

		return bestSoluzione;
	}

	/**
	 * RICORSIONE: Soluzione completa: numero giorni totali ==livello
	 * 
	 * casi terminali: 1. check se sono consecutivi oppure no, etc... 2. costo nuovo
	 * < costo vecchio 3. check se rispetta i giorni massimi Aggiornamento: richiamo
	 * ed elimino
	 * 
	 * @param parziale
	 * @param l
	 */
	private void trovaSequenza(List<Rilevamento> parziale, int l) {

		// Soluzione è completa

		if (l == NUMERO_GIORNI_TOTALI) {

			// controllo se ci sono giorni consecutivi etc..
			if (!this.checkValida(parziale)) {
				return;
			}

			// se trovo un costo migliore lo aggiorno
			double costo = sommaCosti(parziale);
			if (bestCosto == 0.0) {
				bestCosto = costo;
				bestSoluzione = new ArrayList<>(parziale);
			} else if (costo < bestCosto) {
				bestSoluzione = new ArrayList<>(parziale);
				bestCosto = costo;
			}
			return;
		}

		// superata la soglia controllo il giorno massimo
		if (l > NUMERO_GIORNI_CITTA_MAX) {
			if (!this.checkGiorniMax(parziale)) {
				return;
			}
		}

		for (Rilevamento r : meteo.get(l)) {
			parziale.add(r);
			this.trovaSequenza(parziale, l + 1);
			parziale.remove(r);
		}

	}

	/**
	 * calcolo somma dei costi
	 * 
	 * @param parziale
	 * @return
	 */
	public double sommaCosti(List<Rilevamento> parziale) {
		// TODO Auto-generated method stub
		int somma = 0;
		for (Rilevamento r : parziale)
			somma += COST + r.getUmidita();

		return somma;
	}

	/**
	 * VERIFICO: 1. Soluzione parziale non sia maggiore di 15
	 * 
	 * 2. Giorni consecutivi minimi siano rispettati (3) 2.A località di i uguale a
	 * località di i-1
	 * 
	 * 3. Giorni max rispettati
	 * 
	 * @param parziale
	 * @return true se va tutto bene se no false.
	 */

	private boolean checkValida(List<Rilevamento> parziale) {

		if (parziale.size() > 15) {
			return false;
		}

		if (!this.checkGiorniMax(parziale)) {
			return false;
		}

		int index = 1;
		for (int i = 1; i < parziale.size(); i++) {
			if (parziale.get(i).getLocalita().equals(parziale.get(i - 1).getLocalita())) {
				index++;
			} else {
				if (index >= NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
					index = 1;
				} else {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Verifico che non ci sia stato più di 6 giorni in una città sia consecutivi
	 * che non.
	 * 
	 * @param parziale
	 * @return
	 */
	private boolean checkGiorniMax(List<Rilevamento> parziale) {

		int gDay = 0;
		int tDay = 0;
		int mDay = 0;

		for (Rilevamento r : parziale) {

			String localita = r.getLocalita();

			switch (localita) {
			case "Genova":
				gDay++;
				break;
			case "Torino":
				tDay++;
				break;
			case "Milano":
				mDay++;
				break;
			}
		}

		if (gDay > NUMERO_GIORNI_CITTA_MAX || mDay > NUMERO_GIORNI_CITTA_MAX || tDay > NUMERO_GIORNI_CITTA_MAX) {
			return false;
		}

		return true;
	}

}
