package de.minetrain.devlinbot.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Messages {
	private final List<String> sillySentences = new ArrayList<String>();
	private final List<String> streamDownSentences = new ArrayList<String>();
	private final List<String> streamUpSentences = new ArrayList<String>();
	private final String streamDown = " - Stream Offline";
	private final String streamUp = " - Stream läuft wieder";
	private final Random random = new Random();
	
	//{USER}
	//{STREAMER}
	//{TIME}
	public Messages() {
		sillySentences.add("{USER} sag mal... Heißt Tomatenmark Tomatenmark weil Tom Tomaten mag?");
		sillySentences.add("{USER} Werden Krebse eigendlich von Krebserregenden Stoffen erregt?");
		sillySentences.add("Das Meer ist ganz klar eine Suppe!");
		sillySentences.add("{USER} Wenn man sein Ohr ganz leicht auf die Herdplatte drückt, kann man riechen wie dumm man ist?");
		sillySentences.add("Mich braucht jeder! Zumindest behaupten alle ich hätte gerade noch gefehlt‍");
		sillySentences.add("{USER} So So... Ein Thermomix ist also eine Küchenmaschine? Hätte ja auch der heizungsinstallateur bei Asterix und obelix sein können! SeemsGood");
		sillySentences.add("Manche Menschen sind wie schnitzel! Nicht zäh sondern beidseitig bekloppt!");
		sillySentences.add("{USER} Du bist die Stradivari unter den arschgeigen!");
		sillySentences.add("Ich bin soooo satt. Ich mag keinen.... Oh Kuchen!");
		sillySentences.add("Was Schaust mich so schräg an?");
		sillySentences.add("{STREAMER} der böse {USER} war gemein zu mir ;C");
		sillySentences.add("{USER} Willst du das ich gehe oder wie?");
		sillySentences.add("Ich möchte Hünchen mit REIS!");
		sillySentences.add("{USER} was habe ich den nun schon wieder gemacht?");
		sillySentences.add("{USER} es ist bereits {TIME}. Du musst doch langsam ins bett!");
		sillySentences.add("Warum liegt hier eigentlich Stroh? Und warum hat {USER} gepupst?");
		sillySentences.add("{USER} hat gepupst! NotLikeThis");

		streamDownSentences.add("Klingelt {STREAMER}`s Handy etwa schon wieder?"+streamDown);
		streamDownSentences.add("Der Stream hat sich verabschiedet. Er ist bestimmt gleich zurück! {STREAMER}"+streamDown);
		streamDownSentences.add("{STREAMER} ist bestimmt grade in den Urlaub gefahren!"+streamDown);
		streamDownSentences.add("{STREAMER} hat eine Pause eingelegt! Er braucht scheinbar etwas Zeit für sich!"+streamDown);
		streamDownSentences.add("{STREAMER} ist wohl in einen Tunnel gefahren! Das Signal mag tunnel scheinbar nicht."+streamDown);
		streamDownSentences.add("{STREAMER} hat sich verlaufen! Er sucht grade den Weg zurück."+streamDown);
		streamDownSentences.add("{STREAMER} scheinbar eingeschlafen. Er träumt grade von uns!"+streamDown);
		streamDownSentences.add("{STREAMER} hat Hunger! Bevor er uns isst, geht er etwas zu essen hollen."+streamDown);
		streamDownSentences.add("{STREAMER} ist auf eine Mission gegangen. Er muss die Welt retten!"+streamDown);
		streamDownSentences.add("{STREAMER} ist unsichtbar geworden! Er spielt Verstecken mit uns."+streamDown);

		streamUpSentences.add("{STREAMER} ist schon wieder live? PogChamp"+streamUp);
		streamUpSentences.add("{STREAMER} ist zurück aus dem Urlaub. Er hat uns aber nichts mitgebracht!"+streamUp);
		streamUpSentences.add("{STREAMER} hat seine Pause beendet. Er ist bereit, weiterzumachen!"+streamUp);
		streamUpSentences.add("Das Signal hat sich mit dem Tunnel vertagen! {STREAMER} darf jetzt endlich weiter Streamen!"+streamUp);
		streamUpSentences.add("{STREAMER} hat den Weg zurückgefunden! Er war sooo lange weg. ;C"+streamUp);
		streamUpSentences.add("{STREAMER} ist aufgewacht. Er hat wirklich von uns geträumt!"+streamUp);
		streamUpSentences.add("{STREAMER} ist jetzt satt. Er hat etwas Leckeres gegessen!"+streamUp);
		streamUpSentences.add("{STREAMER} hat seine Mission erfüllt. Er hat die Welt gerettet! (Bestimmt...)"+streamUp);
		streamUpSentences.add("{STREAMER} ist sichtbar geworden. Er hat das Versteckspiel gewonnen!"+streamUp);
		streamUpSentences.add("{STREAMER} ist wieder da. Er hat uns wirklich vermisst!"+streamUp);
	}
	
	
	public String getRandomSillySentences() {
		return sillySentences.get(random.nextInt(0, sillySentences.size()-1));
	}
	
	public String getRandomStreamDownSentences() {
		return streamDownSentences.get(random.nextInt(0, streamDownSentences.size()-1));
	}
	
	public String getRandomStreamUpSentences() {
		return streamUpSentences.get(random.nextInt(0, streamUpSentences.size()-1));
	}
}
