# P.I.G. - Client

## Indice
*	[Introduzione](#Introduzione)
*	[Funzionamento](#Funzionamento)
*	[GreenHouse](#GreenHouse)
*	[Classi Scheduler](#Classi-Scheduler)
*	[Server e Threads, into the wild](#Server-e-Threads,-into-the-wild)

## Introduzione
Il programma Server del progetto consiste in un architettura capace di

*	Riceve connessioni in ingresso da svariati client contemporaneamente
*	Gestire più processi per ogni client
*	Gestire le varie attitività e regole impostate dai client
*	Interfacciarsi con una demo realizzata ad hoc per simulare il comportamento di una serra.

All'interno del programma vi sono molteplici classi, a seguire verranno accennnati alcuni dettagli delle più significative.

## Funzionamento
Nel concreto una volta avviato il server, si apre un GUI all'interno della quale vi sono i sensori emulati, che consistono in un JSlider modificabile dall'utente (al fine di simulare cambiamenti percepiti dai sensori), i devices emulati, che consistono anch'essi in JSlider, ma non modificabili dall'utente, bensì solo da attività o regole ed infine una console per i possibili logs di gestione.

## GreenHouse
É una delle classi principali e si occupa di istanziare i vari oggetti (sensori e device) e collegarli al modulo di simulazione.

## Classi Scheduler
Le classi scheduler consistono in ActivityScheduler e RuleScheduler che si occupano di gestire la schedulazione rispettivamente delle attività e dell regole.

### ActivityScheduler
Utilizza la classe di Java ScheduledThreadPoolExecutor per istanziare uno scheduler che avvia le varie attività con l'impostazione "scheduleAtFixedRate", per garantire le ripetizioni delle attività, con un periodo deciso dagli utenti.

### RuleScheduler
Le regole sono profondamente diverse dalle attività, infatti non vengono eseguite a ripetizione, ma rimangono sempre attive in background e permettono un'accensione ed un relativo spegnimento a seguito di valori rilevati dai sensori che superano dei valori soglia (scelti dall'utente).

## Server e Threads, into the wild
Il server, una volta avviato, costruisce la GreenHouse, il simulatore, i due scheduler ed attende una connessione sulla porta 2500 (scelta per comodità in modo che non interferisca con applicativi che usano porte note). Per ogni connessione che viene ricevuta in ingresso viene aperto un thread che gestisce tutte le possibili interazioni con ognuno dei client, singolarmente. Inoltre vengono inviati Summary e State che corrispondo all'elenco delle activity e delle regole già presenti sul server, e all'elenco di tutti i sensori e dispositivi presente al momento dell'invio nella serra. Questi due messaggi verrano poi inviati ripetutamente ogni X minuti ad ogni client, in modo tale che siano tutti aggiornati su aggiunte e modifiche varie a sensori, devices, attività e regole

