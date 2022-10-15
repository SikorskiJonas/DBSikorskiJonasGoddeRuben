# DBSikorskiJonasGoddeRuben
## https://mermaid.live/edit#pako:eNqVVMtugzAQ_BW019KIV5rE1z5ObS89VKqQqhVeiJtgkDFK2yj_XiemQB0FqVxshvGMPbtmD1nFCRhkW2yaO4GFwjKVnnkeq5qUl0KYwvW1GfyrFAxY1c9tWZKypHuNdU1e93ngPRGnHanNL--VeKOV-OCuoBWYFnNNH1qZaUGu1KRnr2iIt6ipqJQwtpZ5Orw98N4ix0dw5gmpB0AilswzmgO0Jcq1sXDggiQndcaVhaYz5k5ka-2gnmfnh_H--vNM7ZGjbt1N1ltE3Thg9hvCu-C92dhuiPOfmeS2PJd0h2pO6e4QFbdhCVmMQjSrj4GPEVO2S2a2paaMMG80SreAO-KnqC_pdi04JdzFMBJ2NPo2nFLpyzTWAR9MfCUKbm7uaXUKek0lpcDMlKPapJDKIw9bXb18yQyYVi350NamQ6i76MBy3DYGrVG-VdWfd2B7-AQWzePZah7EYRxGcRzPFz58AUuC2TJMouhmvoqSOEjigw_fJ4FgtjLEIAyX0SK5WS4XkQ_Eha7UU_ejOQ6HH53ATjs

#### 1. Algemene structuur
Deze schematische weergave van de database bestaat uit 5 onderdelen: loper, loopnummer, etappe, wedstrijd en werknemer

#### 2.1 Loper
In de 'loper' klasse zitten zes eigenschappen: naam, lengte, gewicht, geslacht, leeftijd en loperID.
De eerste vijf eigenschappen worden als string opgeslagen om (menselijke) fouten bij registratie te voorkomen.
Een kommateken bij 'gewicht' kan dan bijvoorbeeld zowel een ',' als een '.' zijn, zonder dat dit gevolgen heeft voor de database.
De loperID wordt automatisch gegenereerd, en wordt gebruikt om een loper en zijn/haar loopnummer te koppelen.

#### 2.2 Loopnummer
De relatie tussen loper en etappe is meer op meer, want een loper kan deelnemen aan meerdere etappes en er nemen meerdere lopers deel aan een etappe.
Een meer op meer relatie is echter complex in een database. Hiervoor wordt de tussenklasse loopnummer gebruikt.
Elk loopnummer heeft één loper, en een loper kan meerdere loopnummers hebben. De loopnummers zijn namelijk uniek per wedstrijd.
Ook zijn er dan meerdere loopnummers voor een bepaalde etappe. Een loopnummer neemt dus deel aan één etappe per keer.
De eigenschappen van een loopnummer zijn: de waarde of het getal; de looptijd of hoelang dit nummer over de etappe gedaan heeft;
de loperID of de loper waaraan het nummer gekoppeld is; de etappeID of de etappe waaraan het nummer deelneemt.

#### 2.3 Etappe
Een etappe is een onderdeel van de loopwedstrijd. Er geldt dus een één op meer relatie tussen etappe en wedstrijd.
Een wedstrijd heeft meerdere etappes, en een etappe hoort slechts bij één wedstrijd.
Wij gaan er in onze structuur dus van uit dat elke database uniek is, en niet herbruikt wordt in verschillende wedstrijden.
De eigenschappen van een etappe zijn: de afstand; de wedstrijdID om weer te geven bij welke wedstrijd de etappe hoort; de etappeID

#### 2.4 Wedstrijd
De wedstrijd is waar het allemaal om draait. De lopers nemen deel aan een wedstrijd door deel te nemen aan de etappes die er deel van uitmaken.
Een wedstrijd heeft ook enkele eigenschappen zoals: de datum; de startplaats en de categorie (straatloop, veldloop,...)

#### 2.5 Medewerker
Naast lopers zijn er op een wedstrijd ook (vrijwillige) medewerkers. Een medewerker heeft een functie, naam, geslacht en leeftijd.
Er geldt een meer op meer relatie tussen medewerker en wedstrijd. Er is dus een tussenklasse nodig om deze relatie in de database
mogelijk te maken.
