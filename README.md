# DBSikorskiJonasGoddeRuben
## https://mermaid.live/edit#pako:eNqdkz1vgzAQhv8KujVk6MqcDpXaLh26eDnZB3GDP2QboQjx32vjEEhIM_QWzKvXz33BANwIggp4i94fJDYOFdNFjHdjyRUMXhjs9_GxYxA1Yz87pcjNnvk9GcvdxZoOxWtAayn7vkn44OSP2No-SFBP7jQjb6yPeNk21ZtrHLKSQiOqqoj3F6klqkPEbWTdBLoTG-olP4aN6lvcym3K_XaoCqlDVsfb4q6drArkGKgxTt5n7mfvM-AyqhWx7jQP_-Mt21vxekQnMk7qZt2usXmOV9zjKaSgaVfPcudtDk-v_N1ICqx9QL3e6wglxGYUShG_6InNIBxJEYMqHgW6EwOmkw-7YL7OmkMVXEcldFbE1Vx-AKhqbD2Nvwe38g0

###### 1. Algemene structuur
Deze schematische weergave van de database bestaat uit 5 onderdelen: loper, loopnummer, etappe, wedstrijd en werknemer

###### 2.1 Loper
In de 'loper' klasse zitten 6 eigenschappen: naam, lengte, gewicht, geslacht, leeftijd en loperID.
De eerste vijf eigenschappen worden als string opgeslagen om (menselijke) fouten bij registratie te voorkomen.
Een kommateken bij 'gewicht' kan dan bijvoorbeeld zowel een ',' als een '.' zijn, zonder dat dit gevolgen heeft voor de database.
De loperID wordt automatisch gegenereerd, en wordt gebruikt om een loper en zijn/haar loopnummer te koppelen.

###### 2.2 Loopnummer
De relatie tussen loper en etappe is meer op meer, want een loper kan deelnemen aan meerdere etappes en er nemen meerdere lopers deel aan een etappe.
Een meer op meer relatie is echter complex in een database. Hiervoor wordt de tussenklasse loopnummer gebruikt.
Elk loopnummer heeft één loper, en een loper kan meerdere loopnummers hebben. De loopnummers zijn namelijk uniek per wedstrijd.
Ook zijn er dan meerdere loopnummers voor een bepaalde etappe. Een loopnummer neemt dus deel aan één etappe per keer.
De eigenschappen van een loopnummer zijn: de waarde of het getal; de looptijd of hoelang dit nummer over de etappe gedaan heeft;
de loperID of de loper waaraan het nummer gekoppeld is; de etappeID of de etappe waaraan het nummer deelneemt.

###### 2.3 Etappe
Een etappe is een onderdeel van de loopwedstrijd. Er geldt dus een één op meer relatie tussen etappe en wedstrijd.
Een wedstrijd heeft meerdere etappes, en een etappe hoort slechts bij één wedstrijd.
De eigenschappen van een etappe zijn: de afstand; de wedstrijdID om weer te geven bij welke wedstrijd de etappe hoort; de etappeID

###### 2.4 Wedstrijd
De wedstrijd is waar het allemaal om draait. De lopers nemen deel aan een wedstrijd door deel te nemen aan de etappes die er deel van uitmaken.
Een wedstrijd heeft ook enkele eigenschappen zoals: de datum; de startplaats en de categorie (straatloop, veldloop,...)

###### 2.5 Medewerker
Naast lopers zijn er op een wedstrijd ook (vrijwillige) medewerkers. Een medewerker heeft een functie, naam, geslacht en leeftijd.
Er geldt een meer op meer relatie tussen medewerker en wedstrijd. Er is dus een tussenklasse nodig om deze relatie in de database
mogelijk te maken.
