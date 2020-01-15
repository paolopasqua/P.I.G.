# P.I.G. - Client

## Indice
*	[Introduzione](#Introduzione)
*	[Modello MVC](#Modello-MVC)
*	[Finestre grafiche](#Finestre-grafiche)
    *	[Finestra di connessione](#Finestra-di-connessione)
    *	[Finestra principale o visione stato](#Finestra-principale-o-visione-stato)
    *	[Finestra delle impostazioni](#Finestra-delle-impostazioni)
    *	[Finestra di inserimento attività](#Finestra-di-inserimento-attività)
    *	[Finestra di inserimento regola](#Finestra-di-inserimento-regola)
    *	[Finestra di guida](#Finestra-di-guida)
*	[Note aggiunte in implementazione](#Note-aggiunte-in-implementazione)
    *	[Interfacce ComponentDrawer e StructureDrawer](#Interfacce-ComponentDrawer-e-StructureDrawer)
    *	[Interfaccia CommunicationProtocol](#Interfaccia-CommunicationProtocol)
    *	[Oggetto ThemeDataSource](#Oggetto-ThemeDataSource)
    * [Localizzazione](#Localizzazione)
*  [Torna indietro](../README.md)

## Introduzione
Il programma Client del progetto deve permettere diverse funzioni base

*	Connessione ed impostazione server
*	Visualizzare lo stato della serra
*	Inserire attività pianificate o da eseguire
*	Inserire regole di automazione

e al contempo restare intuitivo e semplice per l’utente finale.
La logica di funzionamento viene divisa in due fasi: scelta (‘choice’) e gestione (‘management’). La prima è la scelta dell’utente a quale server connettersi, mentre la seconda è la connessione e gestione dei dati relativi alla serra.
L’insieme di queste esigenze evolve in una struttura composta dalle seguenti maschere di interazione per l’utente:

*	Finestre per fase scelta:
    *	Finestra di connessione
*	Finestre per la fase di gestione:
    * Finestra principale / visione stato
    *	Finestra delle impostazioni (per un'implementazione futura)
    *	Finestra di inserimento attività
    *	Finestra di inserimento regola
*	Finestre in comune:
    *	Finestra di guida (per un'implementazione futura)

spiegate nel dettaglio nei seguenti paragrafi.

## Modello MVC
È qui riportato il modello MVC utilizzato per la progettazione del programma Client:

![Client MVC Model](/images/mvc_client.png)

Lo schema risulta composto dalla collaborazione di più modelli MVC.
Il primo a partire da sinistra è relativo alla fase di scelta, comprendendo tutti e tre gli elementi Model, View e Controller. Il Model contiene i dati utili alla connessione al server inseriti nel tempo, mentre la View li pone all’utente. Il controllore per la scelta si occupa anche di avviare la fase di gestione, istanziando e contenendo i controllori. Il ‘choice controller’ si pone come osservatore per il ‘management controller’, pertanto quest’ultimo avrà anch’esso un riferimento all’istanza del primo controllore.
Il secondo modello, quello centrale, è composto solo da View e Controller. Questo rappresenta la fase di gestione della serra, i cui dati sono ricavati direttamente dal server. Questo non esclude una memorizzazione runtime dei dati ricevuti per il funzionamento, ma i valori non sono salvati localmente.
La View è composta da più finestre, ma la principale, di visione stato, è l’unica a comunicare con il Controller.
Tutte le azioni compiute dall’utente o le gestioni logiche che necessitano di comunicazioni con il server passano dal controllore di gestione a quello di connessione, ultimo modello MVC a destra.
Quest’ultimo elemento è stato introdotto per consentire una netta separazione e sostituibilità tra i componenti del Client. Il suo scopo è di occuparsi dell’invio e della ricezione dei pacchetti al server, segnalando l’arrivo di dati attraverso un meccanismo di osservatori come tra i controllori delle due fasi.

## Finestre grafiche
Qui di seguito sono descritte le maschere implementate per il Client del progetto. Il disegno è approssimativo e l’effettiva realizzazione può variare, ma il contenuto e la logica di funzionamento è quella riportata.

### Finestra di connessione
![Connection form](/images/connection_form.png)
Questa è la prima finestra a presentarsi all’utente. Lo scopo è quello di richiedere i parametri di connessione ed effettuare il collegamento al server in modo da poter procedere con l’esecuzione del programma.
La parte in alto consente l’inserimento di nuovi dati, in seguito si trova invece lo storico delle connessioni con la possibilità di fissare in cima determinati server.
Nel caso l’elenco diventasse troppo lungo da scorrere, è prevista la possibilità di ricerca tramite la parte inferiore.
Ultimo elemento in basso è il bottone di apertura della ‘finestra di guida’.
L’interfaccia è quindi suddivisa in quattro pannelli ed i seguenti eventi scatenabili da parte dell’utente:

*	Evento di connessione tramite i bottoni di connessione e doppio click sul nome del server (al Controller);
*	Evento di marcatura preferito tramite bottone a stella (al Controller);
*	Evento di ricerca tramite bottone di ricerca;
*	Evento di apertura maschera di guida tramite bottone apposito (al Controller).

### Finestra principale o visione stato
![State form](/images/main_form.png)
La maschera più complessa e ricca del Client è questa. Infatti, qui si hanno cinque pannelli i quali in ordine da sinistra a destra e dall’alto al basso sono:

*	Pannello di informazione, per le informazioni scritte sulla serra;
*	Pannello delle regole, per la visione e gestione delle regole presenti;
*	Pannello grafico, per la comprensione immediata dello stato della serra;
*	Pannello delle attività, per la visione e gestione delle attività presenti;
*	Pannello di gestione, per eseguire diverse funzioni di utilità.

Gli eventi presenti sono in numero maggiore:

*	Evento di inserimento regola tramite bottone di aggiunta o click sul sensore nel disegno (apertura Finestra di inserimento regola);
*	Evento di inserimento attività tramite bottone di aggiunta o click sul dispositivo nel disegno (apertura Finestra di inserimento attività);
*	Evento di eliminazione regola tramite bottone di cestino (al Controller);
*	Evento di eliminazione attività tramite bottone di cestino (al Controller);
*	Evento di disconnessione tramite bottone di uscita (al Controller);
*	Evento di apertura maschera delle impostazione tramite bottone ingranaggio (apertura Finestra delle impostazioni);
*	Evento di apertura maschera di guida tramite bottone interrogativo (al Controller).

### Finestra delle impostazioni
![Settings form](/images/settings_form.png)
Finestra lasciata ad una _futuara implementazione_ in modo da consentire di inserire quali sensori e dispositivi sono collegati al server e di definirne le proprietà di funzionamento (esempio per Raspberry le GPIO).
Il tutto si sviluppa in tre colonne che partendo da sinistra sono: elenco degli accessori connessi con possibilità di inserimento in fondo; disegno per rendere immediata la comprensione dell’elenco; proprietà per l’accessorio selezionato.
Nella parte centrale in angolo si trova il pulsante per la maschera di guida.
Gli eventi scatenabili sono:

*	Evento di selezione accessorio tramite click in elenco o nel disegno;
*	Evento di eliminazione accessorio tramite bottone cestino (al Controller);
*	Evento di aggiunta tramite bottone di aggiunta (+);
*	Evento di apertura maschera di guida (al Controller);
*	Evento di salvataggio in chiusura finestra (al Controller).

### Finestra di inserimento attività
![Insert activity form](/images/activity_creation_form.png)
In questa finestra è possibile inserire tutti i dati necessari a pianificare un’attività. La struttura è divisa in tre pannelli che dall’alto si compongono: pannello dati attività; pannello dati temporali per attività; pannello di utilità.
La gestione del lock lato Client si riassume nel mostrare quanto tempo è rimasto e nella possibilità di richiedere ulteriore tempo.
Eventi scatenabili:

*	Evento di apertura maschera di guida tramite bottone apposito (al Controller);
*	Evento di conferma inserimento tramite bottone di conferma (al Controller).

### Finestra di inserimento regola
![Insert rule form](/images/rule_creation_form.png)
Finestra in parte simile a quella precedente per struttura; ha lo scopo di iniziare l’inserimento di una nuova regola.
Qui si trova il pannello di inserimento dati necessari alla regola ed infine dal pannello di utilità.
Il bottone di conferma riporta la scritta ‘NEXT’ in riferimento al fatto che dopo questa finestra l’utente viene riportato in quella di inserimento attività (sprovvista della parte dati temporali) per la conclusione della procedura.
Eventi scatenabili sono:

*	Evento di apertura maschera di guida tramite bottone apposito (al Controller);
*	Evento di proseguimento tramite bottone di conferma (apertura Finestra di inserimento attività).

### Finestra di guida
![Help form](/images/help_form.png)
Ultima maschera prevista, lasciata per una futura implementazione, racchiude una piccola guida di riferimento all’utente sulle procedure eseguibili all’interno del programma.
La struttura si compone di due colonne, ovvero quella a sinistra di indice argomenti e quella a destra dove di fatto sta la guida.
Eventi in questa finestra:
*	Evento di selezione argomento da aprire.

## Note aggiunte in implementazione
Qui in seguito si riportano eventuali note che, in fase di implementazione, si è ritenuto necessarie in documentazione di progetto:

### Interfacce ComponentDrawer e StructureDrawer
Per l’implementazione della parte grafica relativa al disegno della serra nella finestra principale, si è pensato ad introdurre l’oggetto ComponentDrawer.
La funzione di questo elemento è di gestire comodamente il render grafico di qualsiasi componente. Questo oggetto consente di aggiungere un listener per il doppio click sul disegno, impostare un testo di tooltip, avere le dimensioni previste per disegnare il componente e, infine, lanciare la procedura di disegno indicando destinazione e fattore di scala.

I vari componenti hanno un'implementazione di questa interfaccia ciascuno, con un specifico disegno ad hoc (utilizzando la grafica AWT di Java).

Ulteriore interfaccia necessaria è StructureDrawer, la quale prevede l'aggiunta/rimozione dei singoli componenti e il disegno della struttura completa.
L'implementazione di questo oggetto è effettuata realizzando un componente JComponent in modo da poterlo gestire nella grafica Swing delle maschere.

### Interfaccia CommunicationProtocol
Per la comunicazione tra Client e Server si è stabilito un protocollo. Questo potrebbe variare nel tempo e pertanto, per favorire l'adattamento e la retrocompatibilità del programma, si è prevista l'interfaccia CommunicationProtocol.
Questo oggetto trova la sua implementazione nella classe ProtocolV1 che realizza il parser per la versione 1.01 del protocollo di comunicazione.

### Oggetto ThemeDataSource
Per favorire la personalizzazione delle finestre a seconda dei gusti dell'utente è possibile modificare colori e bordi attraverso un file di configurazione posto all'interno della cartella utente al percorso '_.PIG/thmsttngpg.prprt_'.
Queste proprietà vanno modificate prima dell'avvio del programma.
Inizialmente saranno create secondo un tema di default, il quale potendo essere arricchito di altre proprietà viene sempre caricato dal programma ed usato in caso di necessità.

### Localizzazione
Il programma Client è predisposto al funzionamento in lingua inglese ed italiano in base alle impostazioni di sistema.
Nel caso di lingua non gestita, viene caricata la lingua inglese.
L'implementazione della localizzazione avviene attraverso i ResourceBundle del linguaggio Java.
