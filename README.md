# DBSikorskiJonasGoddeRuben
https://mermaid.live/edit#pako:eNqdVMFuozAQ_RU0p65KI8CkSXzdtqd2L3uoVCFFUzwh3oKNjGm2G-XfayAJKW2otL7YjJ_fm3ljvIVUCwIOaY5VdSMxM1gkynPjXpdkvATCBK6u3ORfJuCCuvxVFwWZDnRrsSzJoYJmu8c9kKANmZcD7pFEZY38I4aEHcGALPxINhS9q1VqJfVUwbeafXoO-BMtZdpIJ9sh2-K7grddpBlScE8q2wcUYsG9VzTpGs1FFPzotzJ61tpYWgq0tQO5iU53lSDDvfZceHIsJ5VZGshktJHp2g6intetd6cpH0scS_vLlMoc0VZnqkkPBi2lOKqe6vZW_6dfq66F5_j7jo_yt4hBMHdHrXPkU9g195xcd_HGpHBVWVRiKdWyIPtJdEOibcM5gf2NHVPYO9L7xQ5-DbiOt3eM7djBr_jAB-dbgVK4H79lScCuqaAEuFsKNC8JJKrBYW317zeVAremJh_qsrlG-3cC-ArzykVLVE9af_gGvoW_wKMpmyymAQtZGDHGpjMf3oDHwWQexlF0PV1EMQtitvPhX0sQTBYOGIThPJrF1_P5LPKBhLTaPOzfqWbavQNeF2Fb

#### 1. Algemene info
Dit project beschrijft een entity relationship diagram (ERD) voor een database. Deze database zal gebruikt worden om een software programma te maken dat dient voor de administratie van een sportorganisatie. Deze schematische weergave van de database bestaat uit 7 entiteiten: loper, loopnummer, etappe, wedstrijd, categorie, werknemer en funtie. Voor onze interpretatie van een etappe hebben we ons gebaseerd op de definitie van wikipedia. We nemen dus aan dat elke etappe een wedstrijd opzich is en een eigen begin en eindpunt heeft.  

#### 2.1 Loper
In de 'loper' klasse zitten zes properties: id, geboorteDatum, lengte, gewicht, naam, gender.
- Lengte, gewicht: deze worden als string opgeslagen om (menselijke) fouten bij registratie te voorkomen. Een kommateken bij 'gewicht' kan dan bijvoorbeeld zowel een ',' als een '.' zijn, zonder dat dit gevolgen heeft voor de database.
- Id: dit is in datatype int, wordt automatisch gegenereerd en wordt gebruikt om een loper met zijn/haar loopnummer te koppelen.
- geboorteDatum: Bij de geboorte datum is er voor gekozen om met datatype DATETIME te werken. Dit is gedaan zodat er makkelijk kan gewerkt worden met de dag, maand of jaar apart.
- Gender: Voor het datatype bij gender is er gekozen voor een string met lengte 1. Hierdoor kan er op een simpele en korte manier het gender beschreven worden van lopers door middel van M = male, F = female en O = other.
Een loper heeft een relatie met een loopnummer die in 2.8 beschreven wordt.
Voor de klasse loper zullen er een aantal functies komen, 

#### 2.2 Loopnummer (niet af)
De properties van een loopnummer zijn: id, de waarde of het getal; de looptijd of hoelang dit nummer over de etappe gedaan heeft;
de loperID of de loper waaraan het nummer gekoppeld is; de etappeID of de etappe waaraan het nummer deelneemt.

#### 2.3 Etappe (niet af)
De eigenschappen van een etappe zijn: de afstand; de wedstrijdID om weer te geven bij welke wedstrijd de etappe hoort; de etappeID

#### 2.4 Wedstrijd
De wedstrijd is waar het allemaal om draait. De lopers nemen deel aan een wedstrijd door deel te nemen aan de verschillende etappes.
Een wedstrijd heeft ook enkele properties zoals: een id, de datum; de plaats en de categorieId.
- Id: dit is in het datatype int en wordt automatisch gegenereerd.
- Datum: voor het datatype bij de datum is er gekozen voor DATETIME, dit is gedaan voor het makkelijk initialiseren alsook makkelijk filteren op specifieke datums.
- Plaats: de plaats van de wedstrijd is een beschrijving van het gebied waar alle etappes in liggen. Het gekozen datatype is een string.  
- CategorieId: elke wedstrijd valt in één bepaalde categorie. deze propertie is een datatype int en verwijst naar een bepaalde categorie in de overeenkomstige lijst. 

#### 2.5 categorie



#### 2.6 Medewerker
Naast lopers zijn er op een wedstrijd ook (vrijwillige) medewerkers. Een medewerker heeft een functie, naam, geslacht en leeftijd.
Er geldt een meer op meer relatie tussen medewerker en wedstrijd. Er is dus een tussenklasse nodig om deze relatie in de database
mogelijk te maken.

#### 2.7 funtie

#### 2.8 relaties
De relatie tussen loper en etappe is meer op meer, want een loper kan deelnemen aan meerdere etappes en er nemen meerdere lopers deel aan een etappe.
Een meer op meer relatie is echter complex in een database. Hiervoor wordt de tussenklasse loopnummer gebruikt.
Elk loopnummer heeft één loper, en een loper kan meerdere loopnummers hebben. De loopnummers zijn namelijk uniek per wedstrijd.
Ook zijn er dan meerdere loopnummers voor een bepaalde etappe. Een loopnummer neemt dus deel aan één etappe per keer.

Een etappe is een onderdeel van de loopwedstrijd. Er geldt dus een één op meer relatie tussen etappe en wedstrijd.
Een wedstrijd heeft meerdere etappes, en een etappe hoort slechts bij één wedstrijd.
Wij gaan er in onze structuur dus van uit dat elke etappe uniek is, en niet herbruikt wordt in verschillende wedstrijden.

#### referentielijst
definitie etappe: https://nl.wikipedia.org/wiki/Etappe#:~:text=Een%20etappe%20is%20een%20deel,de%20wielersport%2C%20en%20de%20zeilsport



lengte en gewicht nogsteeds int voor bewerkingen: kijken voor gewichtsklasse etc?

de gelinkte ids als propertie beschrijven of weglaten?

als een loper 12 is een een aantal wedstrijden heeft gedaan met tijden. vervolgends 8 jaar later is deze loper 20 jaar. hoe houden we dan bij dat bij die scores van toen hij 12 was dat hij toen 12 was, want in mijn hoofd als de leeftij word aangepast zal er verloren gaan hoe oud en groot hij ws bij bepaalde wedstrijden ni?
