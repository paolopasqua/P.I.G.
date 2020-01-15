# P.I.G. Communication Protocolo Server ⇄ Client Version 1.01
Di seguito la specifica del protocollo di comunicazione tra Server e Client del progetto P.I.G.

## Indice
* [Casi di comunicazione](#Casi-di-comunicazione)
* [Struttura](#Struttura)
* [Comandi](#Comandi)
* [Parametri](#Parametri)
* [Tempistiche](#Tempistiche)
* [Torna indietro](#README.md)

## Casi di comunicazione
* ___Instaurazione della connessione___: 
in questo caso il Client dovrà semplicemente avviare il Socket Java verso l'indirizzo del Server sulla porta fissata 2500.
Quando la connessione sarà instaurata, il Server provvederà a mandare i dati al Client tramite due pacchetti di 'Sincronizzazione entità' e 'Aggiornamento di stato'. Al ricevimento dei due pacchetti il Client sarà pronto all'uso da parte dell'utente.
Nel caso di Server spento od occupato si avrà un messaggio di 'Connessione rifiutata/Timeout error'.
* ___Aggiunta di entità___:
tra i principali scopi del Client vi è quella di aggiungere attività e regole (in un futuro sviluppo anche sensori e dispositivi), pertanto è necessario un comando di aggiunta. Il pacchetto inviato conterrà come parametri gli oggetti da aggiungere. Sarà cura del Server aggiornare le proprie anagrafiche e inviare al Client un messaggio di conferma.
* ___Rimozione di entità___:
altro scopo del Client è quello di rimuovere attività e regole (in un futuro sviluppo anche sensori e dispositivi), pertanto è necessario un comando di rimozione. Il pacchetto inviato conterrà come parametri gli oggetti da rimuovere. Sarà cura del Server aggiornare le proprie anagrafiche e inviare al Client un messaggio di conferma.
* ___Sincronizzazione entità___:
data la mutevole natura delle anagrafiche del Server, si ritiene necessaria la sincronizzazione temporizzata delle stesse (esempio una volta ogni ora o più). Questa consiste nell'invio da parte del Server della lista aggiornata di tutte le entità presenti (attività e regole) a ciascun Client. Non è prevista una risposta a questo messaggio, in quanto mancato un pacchetto il Client si aggiornerà a quello successivo..
* ___Aggiornamento di stato___:
i valori rilevati dai sensori si prevede varieranno in modo 'veloce'. Si prevede quindi un meccanismo di aggiornamento da Server a Client, con una tempistica 'comoda' (adatta alla velocità prevista). Infatti, ogni tot tempo (che può essere ogni 5 minuti per esempio) il Server dovrà inviare un messaggio a ciascun Client comunicando la lista dei sensori e dispostivi, ciascuno con il suo dato aggiornato. Non si prevede risposta in questo caso, in quanto mancato un pacchetto il Client si aggiornerà a quello successivo.
* ___Disconnessione___:
quando un Client si disconnette invia un messaggio al Server per informarlo. Non è prevista alcuna risposta.
Questo perchè in caso di mancata ricezione dal Server, il Client verrà comunque ritenuto scollegato alla chiusura del Socket Java..
* ___Conferma___:
nell'aggiunta/rimozione di un'entità, il Client resta in attesa di una conferma da parte del Server. Questo messaggio ha scopo di confermare l'operazione richiesta.
* ___Errore___:
nell'aggiunta/rimozione di un'entità, il Client resta in attesa di una conferma da parte del Server. Questo messaggio ha scopo di segnalare il fallimento dell'operazione e ritornare una stringa descrittiva dell'errore.

## Struttura
Un pacchetto di dati deve contenere: un identificativo del pacchetto, il comando e gli eventuali parametri.

Per organizzare una tale struttura si pensa che ogni pacchetto avrà forma:
> **{**_id-pacchetto_**}**_comando_**:**_parametri_

oppure in caso non vi siano parametri:
> **{**_id-pacchetto_**}**_comando_

L'identificativo di pacchetto serve per poter connettere logicamente tra loro le richieste con le risposte.
***Attenzione***: nel caso del linguaggio Java con utilizzo di un BufferedReader è necessario un terminatore di riga '\n' al termine del pacchetto in modo da rilasciare il buffer in arrivo.

Ciascun parametro sarà un oggetto espresso con la seguente regola:
> **{**_id-parametro_**,**_dato_\[**,**_dato_**,**...]**}**

dove le parentesi quadre hanno significato di opzionale, i punti di proseguimento e si ritiene necessario almeno un dato per la significatività del parametro. Il primo valore deve essere un identificativo per consentire al Parser implementato di comprendere la natura dell'oggetto (esempio per indicare se è un sensore, una regola o magari anche semplicemente una stringa.

In caso di più parametri da concatenare non vi è un separatore, in quanto già le parentesi graffe fanno da contenitore e distinguono i parametri tra loro.

In caso di parametri con dati complessi (si pensi alle attività che contengono almeno un'azione al proprio interno), la struttura si annida, ponendo come dato un parametro stesso:
> **{**_id-parametro_**,** **{**_id-parametro-annidato_**,**_dato_\[**,**...]**}** \[**,**...]**}**

La logica di annidazione non si applica se il dato appartiene ad un tipo primitivo (esempi: interi, decimali, stringhe, ...).
***Attenzione***: questo non significa che se il parametro è solo un tipo primitivo non viene applicata la struttura del parametro. Anzi, viene applicata la stessa struttura in modo da identificare che parametro è stato accodato nel pacchetto. (si veda come esempio il caso della stringa nel paragrafo sotto inerente ai parametri)

## Comandi
I comandi sono stringhe di caratteri univoci per identificare l'azione richiesta al ricevente.

Si riporta ora la legenda di comandi utilizzati:

| Caso di comunicazione     | Comando    | Direzione invio  |
| ------------------------- | ---------- | ---------------- |
| Aggiunta entità           | ADD        | Client -> Server |
| Rimozione entità          | REMOVE     | Client -> Server |
| Sincronizzazione entità   | SUMMARY    | Server -> Client |
| Aggiornamento stato       | STATE      | Server -> Client |
| Disconnessione            | DISCONNECT | Client -> Server |
| Conferma                  | ACK        | Server -> Client |
| Errore                    | ERROR      | Server -> Client |

Le operazioni di inserimento (_ADD_) e rimozione (_REMOVE_), invece, richiedono una conferma. Non sempre però l'operazione avrà successo. Pertanto in caso di riuscita il Client riceverà un messaggio di Conferma (_ACK_), mentre se il Server fallisse invierà un messaggio di Errore (_ERROR_) con un'eventuale stringa descrittiva.
Per i comandi _SUMMARY_, _STATE_ e _DISCONNECT_ non sono previste risposte.

## Parametri
I possibili parametri di un messaggio sono, in tipo, finiti e con una struttura definita. Infatti, un parametro avrà obbligatoriamente un identificatore di tipo e almeno un valore.

Il primo serve per distinguere l'entità inviata, in modo che il ricevente sappia che dato sta processando e come gestirlo.
Il tipo è pensato come una stringa di tre caratteri numerici decimali, i cui valori sono divisi per categorie:
* 000 -> 099 : dati generici
* 100 -> 299 : sensori
* 300 -> 699 : dispositivi
* 700 -> 999 : spazi liberi

Di seguito una tabella con ciascun identificatore, la sua descrizione in significato e la struttura del parametro:

| Identificatore tipo | Descrizione | Struttura |
| ------------------- | ----------- | --------- |
| 000                 | Stringa     | **{000,**_valore_**}** |
| 010                 | Attività    | **{010,**_id-attività_**,**_data-esecuzione_**,**_durata_**,**_ripetizione_**,**_parametro-azione_**}** |
| 011                 | Regola      | **{011,**_id-regola_**,**_id-sensore_**,**_comparatore_**,**_dato-comparato_**,**_parametro-azione_**}** |
| 012                 | Azione      | **{012,**_id-dispositivo_**,**_stato-acceso_**,**_stato-spento_**}** |
| 100                 | Sensore temperatura emulato | **{100,**_id-sensore_**,**_valore_**}** |
| 200                 | Sensore acqua emulato | **{200,**_id-sensore_**,**_valore_**}** |
| 300                 | Dispositivo lampada emulato | **{300,**_id-dispositivo_**,**_stato_**}** |
| 350                 | Dispositivo ventola emulato | **{350,**_id-dispositivo_**,**_stato_**}** |
| 400                 | Dispositivo resistenza calore emulato | **{400,**_id-dispositivo_**,**_stato_**}** |
| 500                 | Dispositivo pompa irrigazione emulato | **{500,**_id-dispositivo_**,**_stato_**}** |

Nello specifico, si pongono i seguenti formati:
* _data-esecuzione_: in formato TimeStamp;
* _durata_: numero intero che esprime il valore in minuti;
* _ripetizione_: numero intero che esprime il valore in minuti;
* _stato_ (annessi _stato-acceso_ e _stato-spento_): numero intero che esprime il valore a cui porre il dispositivo.


## Tempistiche
In questo protocollo sono previste delle tempistiche oltre le quali scattano meccanismi automatici.

La prima di queste è la durata della sessione di connessione. Per evitare che il Server continui ad inviare messaggi ad eventuali Client "morti" (ovvero non più connessi, ma di cui il Server ha ancora un riferimento di connessione), dopo **24 ore** dall'ultimo messaggio ricevuto dal Client il Server considererà morto tale canale e chiuderà la comunicazione.

Ulteriore tempistica di timeout è l'attesa di risposta da parte del Server: il Client attenderà **1 minuto** la risposta ad una richiesta di connessione o ad una conferma di inserimento/rimozione. Trascorso questo tempo si riterrà l'operazione fallita.

Un altro meccanismo è la sincronizzazione delle entità. Prevedendo una scarsa variazione di entità dopo la prima impostazione, questa tempistica può essere longeva. Si pensa, infatti, che questa procedura possa avvenire ogni **1 minuto** sul Server.

Infine, la procedura di aggiornamento dello stato dei sensori e dispositivi. Queste informazioni devono essere aggiornate con tempistiche più frequenti della sincronizzazione, perciò ongi **10 secondi** il Server provvederà ad informare tutti i Client connessi con il nuovo valore dei sensori e lo stato del dispositivo.
