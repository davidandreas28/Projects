#ETAPA 1
	Strategia folosită în prima etapă este una destul de simplă. În primul rând
	am evitat aproape orice coliziune între nave. Apoi, la fiecare moment de
	timp, o navă are 3 posibilități:
		- dacă s-a umplut în proporție de 80%-90%, atunci se va întoarce la
			shipyard;
		- dacă celula pe care se află este destul de săracă în Halite, atunci
			se va muta către cea mai bogată celulă liberă din împrejurimi;
		- altfel, va rămâne pe loc și va colecta halite.
	De asemenea, în primele runde se vor crea nave noi de fiecare dată când
	jucătorul are fonduri suficiente.
	Toate constantele folosite pentru implementarea strategiei de mai sus au
	fost alese(prin teste repetate pe mai multe valori) astfel:
		- ENOUGHHALITEFACTOR = 1.2 -> reprezinta numărul la care este împărțit
			numărul maxim de halite pentru a determina dacă nava ar trebui să 
			se întoarcă la shipyard (1000/1.2=833).
		- TURNSFORBUILDINGSHIPS = 100 -> reprezintă numărul de ture pentru care
			se vor crea nave noi.
		- NOTENOUGHHALITEFACTOR = 10 -> reprezinta numărul la care este 
			împărțit numărul maxim de halite pentru a determina când o
			celulă este prea săracă în Halite.
	Implementarea funcțiilor folosite este descrisă în comentarii.
	Probleme:
		- nu se construiește niciun dropoff;
		- am încercat să implementăm un mod de a face nava să se întoarcă
			înapoi la shipyard când a atins pragul stabilit fără să mai
			verifice iar acest lucru, însă câmpul adăugat clasei Ship.cpp nu se
			modifică pe parcursul jocului.