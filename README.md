# DBSikorskiJonasGoddeRuben
[![](https://mermaid.ink/img/pako:eNrFVE1v2zAM_SsGsUOHuYG_0iS-buup3WWHAYMBQ7UYR60tGTK9rAvy3yfbSeQYtXusLrLJx_dIkdIBMsURYsgKVtffBMs1KxPpmPWgKtROAn4Ct7dmc78kYIyq-tGUJeoe9J1YVaFBea3b4h6R4x71yxn3C3lNWjzzMWFPMCLzr8nGoveNzEigpfLe1bTpGeBXRpgrLYxsj-yK7ws-9JZ2CR47QpI1SMbK2PnDdLZj-ibwPltXjk9KacKUM2oMyGxIosQhQnLUNtwfRBcoc8KB75p6L7IdTXgdp_8-Diu5VD5XzWSmVcEY1ROFZuezSwW_KA-1bRc-5Ci7oJS6FGrCohAyf4tj28_QVBV25Gar6BATnSkMBZkejGKKdsqmZPsbMCfJtjUxyVMh0xKpFTfuT9ZvvJpmO7gVUtS7WcgeeTc-U2meLuBcnqfztQLhWWDEdbmMc2yXqXuLD1wwXSiZ4OYd61gSoB2abkNsPjnTLwkkssWxhtTPV5lBTLpBF5qqnYzTswfxlhW1sVZM_lbq6h_iA_yFOFiGi83SC_3QD8IwXK5ceIU48hZrPwqCu-UmiEIvCo8u_OsIvMXGAD3fXwer6G69XgUuIBek9OPp2W234387eaNo)](https://mermaid.live/edit#pako:eNrFVE1v2zAM_SsGsUOHuYG_0iS-buup3WWHAYMBQ7UYR60tGTK9rAvy3yfbSeQYtXusLrLJx_dIkdIBMsURYsgKVtffBMs1KxPpmPWgKtROAn4Ct7dmc78kYIyq-tGUJeoe9J1YVaFBea3b4h6R4x71yxn3C3lNWjzzMWFPMCLzr8nGoveNzEigpfLe1bTpGeBXRpgrLYxsj-yK7ws-9JZ2CR47QpI1SMbK2PnDdLZj-ibwPltXjk9KacKUM2oMyGxIosQhQnLUNtwfRBcoc8KB75p6L7IdTXgdp_8-Diu5VD5XzWSmVcEY1ROFZuezSwW_KA-1bRc-5Ci7oJS6FGrCohAyf4tj28_QVBV25Gar6BATnSkMBZkejGKKdsqmZPsbMCfJtjUxyVMh0xKpFTfuT9ZvvJpmO7gVUtS7WcgeeTc-U2meLuBcnqfztQLhWWDEdbmMc2yXqXuLD1wwXSiZ4OYd61gSoB2abkNsPjnTLwkkssWxhtTPV5lBTLpBF5qqnYzTswfxlhW1sVZM_lbq6h_iA_yFOFiGi83SC_3QD8IwXK5ceIU48hZrPwqCu-UmiEIvCo8u_OsIvMXGAD3fXwer6G69XgUuIBek9OPp2W234387eaNo)

https://mermaid.live/edit#pako:eNrFVE1v2zAM_SsGsUOHuYG_0iS-buup3WWHAYMBQ7UYR60tGTK9rAvy3yfbSeQYtXusLrLJx_dIkdIBMsURYsgKVtffBMs1KxPpmPWgKtROAn4Ct7dmc78kYIyq-tGUJeoe9J1YVaFBea3b4h6R4x71yxn3C3lNWjzzMWFPMCLzr8nGoveNzEigpfLe1bTpGeBXRpgrLYxsj-yK7ws-9JZ2CR47QpI1SMbK2PnDdLZj-ibwPltXjk9KacKUM2oMyGxIosQhQnLUNtwfRBcoc8KB75p6L7IdTXgdp_8-Diu5VD5XzWSmVcEY1ROFZuezSwW_KA-1bRc-5Ci7oJS6FGrCohAyf4tj28_QVBV25Gar6BATnSkMBZkejGKKdsqmZPsbMCfJtjUxyVMh0xKpFTfuT9ZvvJpmO7gVUtS7WcgeeTc-U2meLuBcnqfztQLhWWDEdbmMc2yXqXuLD1wwXSiZ4OYd61gSoB2abkNsPjnTLwkkssWxhtTPV5lBTLpBF5qqnYzTswfxlhW1sVZM_lbq6h_iA_yFOFiGi83SC_3QD8IwXK5ceIU48hZrPwqCu-UmiEIvCo8u_OsIvMXGAD3fXwer6G69XgUuIBek9OPp2W234387eaNo

#### 1. Algemene info
Dit project beschrijft een entity relationship diagram (ERD) voor een database. Deze database zal gebruikt worden om een software programma te maken dat dient voor de administratie van een sportorganisatie. Deze schematische weergave van de database bestaat uit 7 entiteiten: loper, loopnummer, etappe, wedstrijd, categorie, werknemer en funtie. Voor onze interpretatie van een etappe hebben we ons gebaseerd op de definitie van wikipedia. We nemen dus aan dat elke etappe een wedstrijd opzich is en een eigen begin en eindpunt heeft.  

#### 2.1 Loper (laatste zin doen of niet?)
In de 'loper' klasse zitten zes properties: id, geboorteDatum, lengte, gewicht, naam, gender.
- Lengte, gewicht: deze worden specifiek als string opgeslagen om (menselijke) fouten bij registratie te voorkomen. Een kommateken bij 'gewicht' kan dan bijvoorbeeld zowel een ',' als een '.' zijn, zonder dat dit gevolgen heeft voor de database.
- Id: dit is in datatype int, wordt automatisch gegenereerd en wordt gebruikt om een loper met zijn/haar loopnummer te koppelen.
- GeboorteDatum: Bij de geboorte datum is er voor gekozen om met datatype DATETIME te werken. Dit is gedaan voor het makkelijk initialiseren alsook makkelijk filteren op specifieke datums.
- Naam: datatype string.
- Gender: Voor het datatype bij gender is er gekozen voor een string met lengte 1. Hierdoor kan er op een simpele en korte manier het gender beschreven worden van lopers door middel van M = male, F = female en O = other.

Een loper heeft een relatie met een loopnummer die in 2.8 beschreven wordt.

Voor de klasse loper zullen er een aantal functies komen, ...

#### 2.2 Loopnummer (niet af)
De properties van een loopnummer zijn: id, de waarde of het getal; de looptijd of hoelang dit nummer over de etappe gedaan heeft;
de loperID of de loper waaraan het nummer gekoppeld is; de etappeID of de etappe waaraan het nummer deelneemt.

#### 2.3 Etappe (niet af)
De eigenschappen van een etappe zijn: de afstand; de wedstrijdID om weer te geven bij welke wedstrijd de etappe hoort; de etappeID

#### 2.4 Wedstrijd
De wedstrijd is waar het allemaal om draait. De lopers nemen deel aan een wedstrijd door deel te nemen aan de verschillende etappes.
Een wedstrijd heeft ook enkele properties: een id, de datum; de plaats en de categorieId.
- Id: dit is in het datatype int en wordt automatisch gegenereerd.
- Datum: voor het datatype bij de datum is er gekozen voor DATETIME, dit is gedaan voor het makkelijk initialiseren alsook makkelijk filteren op specifieke datums.
- Plaats: de plaats van de wedstrijd is een beschrijving van het gebied waar alle etappes in liggen. Het gekozen datatype is een string.  
- CategorieId: elke wedstrijd valt in één bepaalde categorie. deze propertie is een datatype int en verwijst naar een bepaalde categorie in de overeenkomstige lijst. 

Een wedstrijd heeft een relatie met Etappe en Categorie, deze worden verder besproken in 2.8.

#### 2.5 categorie
De tabel categorie bevat de verschillende categorien van de loopwedstrijden. Voorbeelden van deze categorien zijn bijvoorbeeld: veldloop heren, veldloop junioren, marathon senioren, obstakelloop vrouwen, obstakelloop junioren,... 
Er zullen voor de software admins ook functies zijn om categorien toe te voegen, aan te passen of verwijderen.
De entity Categorie bevat twee properties: id en categorie.
- Id: dit is in het datatype int en wordt automatisch gegenereerd.
- Categorie: dit is van het datatype string en beschrijft in een paar woorden de bepaalde categorie.

een categorie heeft een relatie met Wedstrijd deze word verder besproken in 2.8.

#### 2.6 Medewerker (funtieid aanpassen zodat medewerker kan meerdere functies?)
Naast lopers zijn er op een wedstrijd ook (vrijwillige) medewerkers. Een medewerker heeft een id, naam, gender, geboortedatum, datum van tewerkstelling en een functieId.
- Id: dit is in het datatype int en wordt automatisch gegenereerd.
- Naam: datatype string.
- Gender: Voor het datatype bij gender is er gekozen voor een string met lengte 1. Hierdoor kan er op een simpele en korte manier het gender beschreven worden van lopers door middel van M = male, F = female en O = other. 
- Geboortedatum: datatype datetime. Dit is gedaan voor het makkelijk initialiseren alsook makkelijk filteren op specifieke datums.  
- Datum van tewerkstelling: datatype datetime. Dit is ook gekozen voor het makkelijk initialiseren
- FunctieId: elke medewerker heeft een bepaalde functie, deze functieId verwijst via een int naar een bepaalde functie in de tabel met alle functies.


#### 2.7 funtie
Er is gekozen voor een apart entity voor functie zodat bij functieindelingen en medewerker initialisatie zeker alle functies hetzelfde beschreven worden. Als de functie steeds een vrijblijvende string is kan het gebeuren dat iemand de functie "bewaking" heeft en iemand anders "bewaker" of "beveiliger" dit zal zorgen voor moeilijkheden bij filteren op functies bij medewerkers etc. Een eigen entity geeft ook als voordeel dat er voor de software eigenaars functies gemaakt kunnen worden waardoor zij makkelijk functies kunnen toevoegen of verijderen. Functie bevat twee properties: id en functie.
- Id: dit is in het datatype int en wordt automatisch gegenereerd.
- Functie: dit is een datatype string en is een één woord bescrhijving van de functie.


#### 2.8 relaties
De relatie tussen loper en etappe is meer op meer, want een loper kan deelnemen aan meerdere etappes en er nemen meerdere lopers deel aan een etappe.
Een meer op meer relatie is echter complex in een database. Hiervoor wordt de tussenklasse loopnummer gebruikt.
Elk loopnummer heeft één loper, en een loper kan meerdere loopnummers hebben. De loopnummers zijn namelijk uniek per wedstrijd.
Ook zijn er dan meerdere loopnummers voor een bepaalde etappe. Een loopnummer neemt dus deel aan één etappe per keer.

Een etappe is een onderdeel van de loopwedstrijd. Er geldt dus een één op meer relatie tussen etappe en wedstrijd.
Een wedstrijd heeft meerdere etappes, en een etappe hoort slechts bij één wedstrijd.
Wij gaan er in onze structuur dus van uit dat elke etappe uniek is, en niet herbruikt wordt in verschillende wedstrijden.

Er geldt een meer op meer relatie tussen medewerker en wedstrijd. Er is dus een tussenklasse nodig om deze relatie in de database
mogelijk te maken.

#### referentielijst
definitie etappe: https://nl.wikipedia.org/wiki/Etappe#:~:text=Een%20etappe%20is%20een%20deel,de%20wielersport%2C%20en%20de%20zeilsport



willen we lengte en gewicht en leeftijd nogsteeds als int voor bewerkingen te kunnen doen: kijken voor gewichtsklasse en leeftijd categorariseren etc?

de gelinkte ids als propertie beschrijven of weglaten?

als een loper 12 is een een aantal wedstrijden heeft gedaan met tijden. vervolgends 8 jaar later is deze loper 20 jaar. hoe houden we dan bij dat bij die scores van toen hij 12 was dat hij toen 12 was, want in mijn hoofd als de leeftij word aangepast zal er verloren gaan hoe oud en groot hij ws bij bepaalde wedstrijden ni?

kan een medewerker niet meerdere functie hebben, een wedstrijd dit, andere wedstrijd dat? zo ja hoe toepassen in ERD?
