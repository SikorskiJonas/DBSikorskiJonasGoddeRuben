# DBSikorskiJonasGoddeRuben
https://mermaid.live/edit#pako:eNqdVMFuozAQ_RU0p65KI8CkSXzdtqd2L3uoVCFFUzwh3oKNjGm2G-XfayAJKW2otL7YjJ_fm3ljvIVUCwIOaY5VdSMxM1gkynPjXpdkvATCBK6u3ORfJuCCuvxVFwWZDnRrsSzJoYJmu8c9kKANmZcD7pFEZY38I4aEHcGALPxINhS9q1VqJfVUwbeafXoO-BMtZdpIJ9sh2-K7grddpBlScE8q2wcUYsG9VzTpGs1FFPzotzJ61tpYWgq0tQO5iU53lSDDvfZceHIsJ5VZGshktJHp2g6intetd6cpH0scS_vLlMoc0VZnqkkPBi2lOKqe6vZW_6dfq66F5_j7jo_yt4hBMHdHrXPkU9g195xcd_HGpHBVWVRiKdWyIPtJdEOibcM5gf2NHVPYO9L7xQ5-DbiOt3eM7djBr_jAB-dbgVK4H79lScCuqaAEuFsKNC8JJKrBYW317zeVAremJh_qsrlG-3cC-ArzykVLVE9af_gGvoW_wKMpmyymAQtZGDHGpjMf3oDHwWQexlF0PV1EMQtitvPhX0sQTBYOGIThPJrF1_P5LPKBhLTaPOzfqWbavQNeF2Fb

#### 1. Algemene info
Dit project beschrijft een entity relationship diagram (ERD) voor een database. Deze database zal gebruikt worden om een software programma te maken dat dient voor de administratie van een sportorganisatie. Deze schematische weergave van de database bestaat uit 7 entiteiten: loper, loopnummer, etappe, wedstrijd, categorie, werknemer en funtie. 

#### 2.1 Loper
In de 'loper' klasse zitten zes properties: id, geboorteDatum, lengte, gewicht, naam, gender.
- Lengte, gewicht: deze worden als string opgeslagen om (menselijke) fouten bij registratie te voorkomen. Een kommateken bij 'gewicht' kan dan bijvoorbeeld zowel een ',' als een '.' zijn, zonder dat dit gevolgen heeft voor de database.
- Id: dit is in datatype int, wordt automatisch gegenereerd en wordt gebruikt om een loper met zijn/haar loopnummer te koppelen.
- geboorteDatum: Bij de geboorte datum is er voor gekozen om met datatype DATETIME te werken. Dit is gedaan zodat er makkelijk kan gewerkt worden met de dag, maand of jaar apart.
- Gender: Voor het datatype bij gender is er gekozen voor een string met lengte 1. Hierdoor kan er op een simpele en korte manier het gender beschreven worden van lopers door middel van M = male, F = female en O = other.
relaties.....

#### 2.2 Loopnummer neeeeeeeeeee
De properties van een loopnummer zijn: id, de waarde of het getal; de looptijd of hoelang dit nummer over de etappe gedaan heeft;
de loperID of de loper waaraan het nummer gekoppeld is; de etappeID of de etappe waaraan het nummer deelneemt.

#### 2.3 Etappe neeeeeeeeeeee
De eigenschappen van een etappe zijn: de afstand; de wedstrijdID om weer te geven bij welke wedstrijd de etappe hoort; de etappeID

#### 2.4 Wedstrijd
De wedstrijd is waar het allemaal om draait. De lopers nemen deel aan een wedstrijd door deel te nemen aan de etappes die er deel van uitmaken.
Een wedstrijd heeft ook enkele eigenschappen zoals: de datum; de startplaats en de categorie (straatloop, veldloop,...)

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

links:
etappe https://www.woorden.org/woord/etappe



lengte en gewicht nogsteeds int voor bewerkingen: kijken voor gewichtsklasse etc?

de gelinkte ids als propertie beschrijven of weglaten?
