package de.minetrain.devlinbot.userinput.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.minetrain.devlinbot.config.obj.JokeSystemSettings;
import de.minetrain.devlinbot.resources.RandomArrayList;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class WitzAPI {
	private static final Logger logger = LoggerFactory.getLogger(WitzAPI.class);
	private static final String userAgent = "DevlinBot (https://github.com/MineTrainDevelopment/devlin)";
	private final RandomArrayList<String> jokes = new RandomArrayList<String>();
	private final List<String> categorys;
	private final JokeSystemSettings settings;
	
	public WitzAPI(JokeSystemSettings settings) {
		this.settings = settings;
		logger.debug("Request a language check for witzapi.de!");
		HttpResponse<String> language = Unirest.get("https://witzapi.de/api/language/?abbreviation="+settings.getLanguage())
			  .header("Accept", "*/*")
			  .header("User-Agent", userAgent)
			  .asString();
		
		String languageBody = language.getBody().replaceAll("[\\[\\]{}]", "");
		if(languageBody.isEmpty() || !languageBody.contains(",")){
			logger.warn("Unsupported language code! -> '"+settings.getLanguage()+"' Try 'en' or 'de' instad!");
			this.categorys = new ArrayList<String>();
			settings.setAktiv(false);
			return;
		}
		
		logger.debug("Request the avalible joke categorys");
		HttpResponse<String> categoryResponds = Unirest.get("https://witzapi.de/api/category/?language=" + settings.getLanguage())
				.header("Accept", "*/*")
				.header("User-Agent", userAgent)
				.asString();
		
		this.categorys = Arrays.asList(categoryResponds.getBody()
				.replaceAll("[\\[\\]{}]", "")
				.replace("\"name\":", "")
				.replace("\"language\":\"de\",", "")
				.replace("\"language\":\"de\"", "")
				.replace("\"", "")
				.split(","));
		
		
		if(categorys.isEmpty()){
			logger.warn("Your chosen language code has no joke categorys available! -> '"+settings.getLanguage()+"' Try 'en' or 'de' instad!");
			settings.setAktiv(false);
			return;
		}
		
		collectNewJokes(100);
	}

	/**
	 * 
	 * @param amound A ruff estimation how many shoult be pullt.
	 */
	public void collectNewJokes(int amound) {
		int jokesPerCategory = amound / categorys.size();
		
		if(jokesPerCategory == 0){
			jokesPerCategory = 2;
		}
		
		logger.info("Requesting "+amound+" new Jokes from witze.de");
		for(String category : categorys){
			HttpResponse<String> jokeResponse = Unirest
				.get("https://witzapi.de/api/joke/?limit=" + jokesPerCategory + "&category=" + category + "&language=" + settings.getLanguage())
				.header("Accept", "*/*")
				.header("User-Agent", userAgent)
				.asString();
			
			List<String> jokes = Arrays.asList(jokeResponse.getBody()
				.replaceAll("[\\[\\]{}]", "")
				.replace("\\n", " ")
				.replace("\"language\":\"de\",", "")
				.replace("\"language\":\"de\"", "")
				.split("\"text\":\""));
			
			
			for(String joke : jokes){
				String regex = "\\b"+"(?:(?:w(?:hite(?:fragilitycankissmyas|y)|armonger|(?:orth|it)les)|(?:(?:m(?:ean\\-?spiritedn|indl|adn)|ridiculousn|foolishn|(?:t(?:hought|op)|cl(?:ass|ue)|s(?:ham|pin)e|head|hope)l|blindn|sillin|feckl|lazin|dumbn)e|(?:bitch|(?:lame|fat))a|c(?:ock|unt)a|jerka|s(?:hit|uck)a|duma|gaya)s|un\\-educatedoop|(?:homosexualis|dummycra|(?:communi|loyali|brea)s|m(?:uslimis|aggo)|hypocra|bitchti|islamis|numb\\-?nu|pigle)t|(?:(?:anti\\-?relig|pretent|o(?:bnox|d))i|sanctimoni|unscrupul|duplicit|trea(?:cher|son)|redicul|hein)ou|(?:psycho\\-?kill|tr(?:ump(?:\\-(?:hump|suck)|hump|suck)|app)|loo(?:ooooos|z)|dickbeat|ga(?:y\\-?ha|ngs)t|kidnapp|stripp|hoard|mooch|scamm|b(?:amm|onk))er|knuckdragger|(?:ku(?:\\-ku\\-|ku)klow|barbaria|glutto|moma)n|(?:know(?:\\-it\\-|it)al|(?:neanderth|genit|illeg)a|eco\\-?radica|infide|radica|fai)l|(?:(?:execution|(?:bungl|lick))e|per(?:petrato|ju)|trumple|i(?:diot|ck)e)r|i(?:\\*d\\*i\\*o\\*t\\*|diot['’]|si)|(?:cockroach|dumbass|jackass)e|(?:d(?:und|iap)er|pot)head|t(?:rumpster'|hieve|e[ae]t)|(?:cun(?:ni|il)ling|ignoramo|cuniling|kuniling|traitoro|cretino|aniling|pompo|bog)u|b(?:(?:ooooooo|aztar)b|ooo(?:oo?b|b)|angbu|strd'|00b)|(?:buttchee|canuc)k|s(?:h(?:i(?:tbrain|ll)|ooting)|t(?:upidit'|ab)|hit\\-head|chumuck|kinhead|atan'|uckee|muck|ck|ob)|(?:(?:fanny|piss)fla|tw(?:atli|er)|shee)p|f(?:ake\\-?new|uckas|ool'|ece)|estupido|m(?:o(?:lester|ron')|ampara)|feminazi|n(?:eo\\-nazi|ard)|coward'|bangbro|c(?:litor|ial|yal)i|orgasmu|libtard|l(?:ackey|eftie)|(?:amoeb|boiol)a|p(?:ussie|impi|ompa)|vacuou|(?:lar|ba)das|doper|high|a(?:sse|\\-s\\-|_s_|nu)|gros|rat'|jugg)s|(?:s(?:ucccccccccccccccckkkkk|hitfuc|pun|tan)|c(?:irclejer|roc|aw)|trash\\-?tal|(?:tit(?:ty)?wa|ska)n|dickmil|f_u_c_|u(?:nthan|c)|voetse|phuc|d(?:or|c)|k(?:aw|oc|k)|mic|ree)k|(?:(?:fuckingshitmother|b(?:rother|unny)|dog\\-)fuck|s(?:tupidestpresidentev|chwanzlutsch|hitbagg|andnigg|wing|uff)|(?:c(?:arpet|ok)mun|redenba)ch|donkeyribb|(?:muthafuck|cockkno|p(?:olesmo|uc)|stal)k|(?:f(?:(?:uckers|annyf)u|udgepa)|a(?:rsch(?:le|fi)|ss(?:cr|j)a|sssu|tta)|muthafe|cockfu)ck|(?:mutherf|tittief|c(?:ock\\-|0ck)s|p(?:enis|ig)f|(?:ass\\-|fag)f)uck|c(?:o(?:ckmongl|ot)|lapp)|(?:penisban|cockbur|dickmon|bug)g|(?:cocksni|penispu)ff|(?:bulls\\*\\*t|shitspit|analrit|kinks)t|(?:c(?:umdump|ockma)|dump)st|unclefuck|ass\\-jabb|s(?:ore\\-?lo|ch(?:ie|ei)s)s|assbang|asshopp|(?:assn|gold)igg|t(?:rickst|oss)|f(?:ur?hr|lam)|b(?:la|ir)th|hitl|voyu|gom)er|(?:t(?:rump(?:\\-as\\-useful\\-|asuseful)idio|w4)|u(?:nintelligen|n\\-?smar|pskir)|(?:embarrasse|fuckshitcu|t(?:hundercu|yra)|(?:malign|repugn|arrog)a|shitcu)n|(?:clown\\-?bea|gayfucki|(?:antichr|chauvin|extrem)i|kackwur|masochi|(?:d(?:ishon|et)|goofi)e|na(?:rcisi|stie)|hornie|ince)s|(?:belliger|fraudul|excrem|petull|insol|torm)en|(?:de(?:epthro|adbe)|ass\\-h)a|(?:(?:cocknu|mcfa)gg|nigl)e|s(?:tupid(?:ies|fa)|hitties|h(?:ootou|1)|ickes|_h_i_|croa|pur|kee)|a(?:nti\\-?lgb|ss(?:shi|ha))|goddamni|(?:bitcho|c(?:u(?:nt|m)sl|amsl|n)|kra)u|d(?:umbes|olcet|af)|(?:cumta|esco)r|f(?:ukwi|ar)|corup|(?:bam|g\\-s)po|squir|inep|nack|cl1|5h[1i]|en)t|(?:religious\\-?hypocrit|(?:(?:(?:passive\\-?aggre|aggres|expul)|delu)|repul)siv|i(?:ncompetenc|sch)|(?:ass(?:aultsra|(?:aultra|wi))|datera)p|t(?:r(?:ansvestit|ip)|winki|ortur)|u(?:n(?:redemptiv|welcom)|rin)|(?:(?:(?:butt\\-p|ass\\-?p)ir|ejakul)a|m(?:a(?:ster\\-|5ter)|45ter)ba|morgenlat|poopchu|(?:cock|ass)bi|roset)t|(?:dick\\-snee|kackbrat|sodomi|f(?:ra|o)t)z|(?:intercour|hackfres|bellico|shithou|cockno|disea|nutca|obtu|möp)s|idiotphob|k(?:ampflesb|ik)|negligenc|s(?:ch(?:abrack|lamp|eiß)|h(?:ithol|ov)|abotag|la(?:ntey|v)|ploog|uicid|poog)|(?:oppressi|kna)v|c(?:lownsho|amelto|oochi|rud|ho[dk])|d(?:emagogu|ruggi|u(?:mbi|nc)|ik)|petulanc|(?:childli|nazi\\-?li|(?:bull)?dy|bukka|k(?:ana|y))k|(?:decep|primi)tiv|(?:loath|three)som|v(?:andaliz|il)|(?:dick(?:jui|fa)|reprodu|c(?:ock|lit)fa|assfa)c|(?:(?:bla|fu)ck|cunt)fac|w(?:horefac|eeni|00s)|(?:camwho|ma(?:ssac|nu))r|i(?:nsec|mmat)ur|(?:creamp|lezz|d(?:ar|oo)k)i|lowlif|h\\-o\\-l\\-|a(?:\\*\\*\\*\\*l|r(?:rs|5))|(?:bond|garb)ag|(?:(?:asin|coca)i|ina)n|fuckm|b(?:rat|on)z|g(?:oats|rop)|flang|(?:hes|duc)h|m(?:ing|ös)|kimm|4r5)e|(?:s(?:(?:elf\\-?fornicati|trap)|implet)o|(?:(?:penis\\-?extens|invas)i|basterdizati|prostituti|curmudge|dominati|b(?:af|u)fo)o|(?:anti\\-?americ|authoritari|machiavelli|pseudo\\-?hum|reptili|hit\\-m|conm|kl)a|masturbatio|d(?:umbed\\-?dow|emo)|ejaculatio|h(?:urensoh|a(?:te\\-?o|rdo))|(?:onanier|flittch|tittch|drunk|madm|popp|rott|sem)e|(?:assgobl|fuckbra|shitsta|blumpk|trumpk|f(?:ring|urk)|hero)i|a(?:\\*\\*|ss)clow|t(?:rumpea|itte)|f(?:rigge|ukk?i|\\*cki|elo)|Goddam|p(?:(?:impe|0)r|aw)|ficke|b(?:ums|eat)e|gokku)n|(?:(?:criminalmental|pathetical|insidious|niggard)l|acismstupidit|anti\\-?militar|ridiculousl|(?:b(?:loodthir|u)s|bea?stiali|i(?:mmoral|nsan)i|mediocri|s(?:tupidi[di]|exuali|hi[*f])|absurdi|nudi)t|(?:stupidityw|pornograp|tus)h|junglebunn|p(?:o(?:rchmonke|tt)|u(?:nann|sss)|laybo|esk|ike)|ludicrousl|(?:cockmonk|knobjok|nobjok|balon)e|(?:c(?:ock|um)joc|hon)ke|hypocris|dumbweaz|k?nobjock|assmonke|(?:octopus|clum|pu\\*s)s|idiocrac|v(?:iciousl|a(?:granc|pidit|nit)|a?jayja)|c(?:owardl|r(?:ybab|app))|s(?:exuall|tupidl|cumm|i(?:ckl|ss))|stupdit|(?:mental|rabid)l|ponypla|f(?:uckbo|a(?:ny|sh)|ox)|man\\-bo|w(?:orksh|hinn)|lo(?:one|us)|grump|manbo|fanbo|twatt|u(?:nru|g)l|hair|beez|fump|f4nn)y|(?:(?:islamaphobeli|pedobe)a|(?:cockmunch|assmunch|dickfuck|(?:assfu|cra)ck|asslick|fook)e|infiltrato|f(?:ornicato|u(?:kk?e|x0)|ecke)|cuntlicke|(?:dicksuck|cumm|kumm)e|buttfucke|fuckbutte|fabricato|bigotryo|m(?:uffdiv|ind)e|oppresse|unpopula|inferio|imposto|vibrato|s(?:h(?:agg|oot)e|tinke)|twunte|fcuke|n(?:(?:1g|e)ge|igg3)|rea)r|(?:rapist(?:\\-in\\-|in)chie|piss(?:ed)?of|(?:j(?:(?:a(?:ck\\-|g)|ack)|erk\\-)o|fucko)f|drump|adol|wt)f|(?:(?:s(?:on\\-of\\-a\\-bit|ten|nat)|anti\\-?fren|arschlo|b(?:uttmun|1[7t]|i\\+)|k(?:ickpun|oot)|panoo|felt|l3i[+t]|crot|eunu|b(?:ia|!)t|b\\*it|mit)c|untermensc|shitbreat|(?:loudmou|cocksmi)t|but(?:tmu)?c|nigg4|doos|gooc|kooc)h|(?:(?:m(?:outhbreath|ung)|pussy\\-?grabb|(?:mass\\-?murde|bluste|scisso)r|(?:(?:blood|scum)\\-suc|(?:boot\\-?li|pussyli|fru)c|(?:blood|scum)suc|tit\\-?suc|lovema|f(?:ux|ri)c)k|un(?:\\-deserv|d(?:eserv|ress)|appeal|think|car)|(?:self\\-?lo|conni)v|(?:snowbal|babb)l|r(?:eichw|uin|imm|av)|s(?:hrimp|icken)|knobb|(?:crin|fig)g|hump|f'k|ly)in|(?:fingerfucki|c(?:yberfuck|(?:(?:ocksu|untli)ck|heat))i|f(?:(?:ornicat|k)i|i(?:nger|st)i|\\*\\*\\*\\*?i|\\-i)|(?:dicksuck|cumm|kumm)i|p(?:enetrati|oonta|hukk?i|eggi)|t(?:erroriz|hiev)i|mu(?:ffdiv|rder)i|fingerba|b(?:u(?:mb|ng)li|rayi)|f(?:elch|\\-\\-k)i|invadi|s(?:c(?:rewi|hlo)|pouti|tinki)|licki|fcuki|lusti|burni|hati|puki)n|masturbatin|(?:manipul|nause|thro)atin|d(?:i(?:sgustin|ckba)|amnin)|(?:manbear|piss)pi|buttplu|frottin|f(?:aggin|uckba|\\-\\-\\-\\-\\-)|commin|lapdo|humbu|r(?:apin|eudi))g|(?:s(?:elf\\-?satisfi|mall\\-?mind|kinn|pew)|(?:(?:feebl|clos)e|narrow)\\-?mind|ill\\-?prepar|(?:ill\\-?mann|mud)er|t(?:h(?:in\\-?skin|reate)n|wofac)|hate\\-?fill|(?:gosh\\-?dar|shitcan)n|(?:emascul|xr)at|god\\-damn|(?:bonehea|pig\\-?hea)d|janusfac|eradicat|un(?:educat|inform)|b(?:arenak|etray)|d(?:imwitt|e(?:rang|lud))|n(?:euter|ak)|dement|fvck|curs|knif|mess|robb|maim)ed|(?:m(?:oth(?:er|a)fucking|asterbation)|(?:(?:finger|(?:cyber|fist))fucke|m(?:oth(?:er|a)fucke|urdere))r|i(?:ncompetent|gnoramu)|(?:ejaculatin|doucheba)g|(?:(?:fistfuck|beat)|kill)ing|(?:knuckle|fuck)head|(?:transgende|predato|offende|invade|(?:grift|monst|loos|hook|bean|deal)e|kille|nigg?e|lia)r|p(?:rostitute|sychopath|(?:edophil|arasit)e|isser|unk)|(?:degenerat|illiterat|sn(?:owfl)?ak|(?:butt|a(?:ss)?)hol|nippl|pub)e|h(?:ypocrite|[ao]g)|xenophobe|(?:charlat|lesbi)an|fucktard|b(?:astardo|itcher|oob)|imbecile|s(?:hit(?:t(?:ing|er)|head|ing)|cumbag|chmuck|exist|teal|icko|ack|lug)|t(?:esticle|roll|hug)|lunatic|jerkoff|buffoon|blowjob|n(?:eonazi|ut(?:ter|job))|orgasim|(?:bollo|pri)ck|kondum|racist|useles|w(?:hiner|acko)|c(?:hump|oon)|dweeb|(?:bimb|dild)o|dink)s|(?:(?:(?:acrotom|dendr)|ur)o|vorare)philia|m(?:ean\\-?spirited|o(?:th(?:erfuck(?:in)?|afuck(?:in|a)?)|ron)|u(?:th(?:er|a)|rder|slim|ff)|ad)|m(?:oth(?:er|a)fucking|asterbation)|e(?:mbarrassment|xecutions|rotic)|m(?:o(?:th(?:afuck(?:in\\\\'|a[sz]|s)|erfucks)|lested|rons|f[0o]|ng)|a(?:st(?:erb(?:at[*3e]|8)|urbate)|5terb8|niacs)|i(?:sogynic|lf)|u(?:rder|slim)s|0f[0o])|(?:s(?:tupids)?elfi|(?:(?:(?:dengl|pigg|rubb)|boor)|oaf)i)sh|(?:(?:opportu|misogy)nisti|(?:anti\\-?cathol|socio\\-path|egocentr|psychot|illog)i|narcissisti|(?:mysogynis|jingois|(?:hom|aut)oero|idioto|spas)ti|un(?:apologe|patrio)ti|s(?:ociopath|ycophant)i|homophobi|egomania|imbecili|p(?:arasiti|th)|idioti|moroni)c|(?:(?:dingleberr|trann|p(?:ant|\\*ss))i|t(?:rumplodit|esticul|wo\\-fac|1tti)|d(?:umb\\*ass|omm)|(?:(?:donuth|(?:hellh|a(?:\\*\\*|\\-)h))o|incurab)l|a\\*\\*\\*\\*\\*|idioci|p\\*\\*\\*\\*i|s(?:hprem|toog)|vultur|l(?:ooni|eech)|\\*itch|cojon|w(?:illi|hr)|yat)es|e(?:mbarrassmen|gotist)|(?:(?:finger|(?:cyber|fist))fucke|m(?:oth(?:er|a)fucke|urdere))r|h(?:ypocrit(?:ical|s)|o(?:(?:mophob|ar)e|r(?:rific|e))|ed)|(?:(?:finger|(?:cyber|fist))fucke|m(?:oth(?:er|a)fucke|urdere))d|(?:extermin|castr)ated|(?:opportu|misogy)nists|(?:(?:meterosexu|irration|cryst)a|egotistica|disgracefu|un(?:ration|ethic)a|(?:un(?:tru|fai)th|deceit)fu|ungratefu|genocida|fanatica|n(?:umbskul|ippe)|c(?:esspoo|rue)|maniaca|goofbal|s(?:hitfu|chi)l|t(?:estica|ubgir)|vengefu|hatefu|camgir|feca|pope|e(?:vi|l)|ora)l|(?:cockmongru|a(?:nti\\-?isra|mp)|d(?:ickweas|öd)|sc(?:hwucht|oundr)|lümm|pimm)el|(?:(?:st(?:o(?:ne\\-?stu|oooo)p|ewp)|p(?:arano|utr)|ranc|fet)i|(?:(?:(?:b(?:assackw|lowh)|blow\\-h|d(?:ie\\-?h|ull|ot)|fagt)|bst)a|gay(?:lo|ta))r|(?:gangbang|pervert|retard|crook)e|(?:brain\\-?d|peckerh|cockh|knobh|(?:d\\*ck|meth)h|nobh)ea|(?:peanut|rag)hea|horrifie|d(?:i(?:sguste|ckw(?:ee|[ao]))|rugge|amne)|goddamne|(?:corrupt|darn)e|t(?:owelhea|wathea|u(?:pi|r)|ar)|(?:underha|babela|axwou|belle|nazia)n|m(?:anchil|uffe)|s(?:ucker|crew)e|(?:(?:dick|ass)he|sh\\*\\*lo|gayw)a|vagabon|f(?:uckwa|oole)|k(?:nobe[an]|icke)|cuckol|p(?:hukk?e|unche|egge)|a(?:sswa|f)|c(?:raze|hoa))d|(?:foul\\-?mout|unwas)hed|w(?:oman\\-?hating|h(?:i(?:te\\-?trash|ny)|ack(?:job|o)|ore(?:bag|s))|o(?:rldsex|p)|retched|i(?:chse[nr]|(?:mp|ll)y)|ankjob|e(?:a(?:sel|k)|irdo)|(?:anke|hoa)r|ähler|an(?:ky|g)|itch)|(?:crap\\-?italis|tribadis|kickthe|s(?:antoru|crotu|exca|&)|(?:gorega|bd)s|erotis|femdo|rectu|ble|jis)m|(?:(?:ho(?:modumb|rse)|ape)sh|chickensh|assband|bullssh|goddamm|maladro|fuc?kwh|bullsh|cocksh|d(?:umbsh|umsh|ipsh|amm)|shizn|herm|nitw|vom)it|(?:(?:douche|cock)waff|un(?:(?:depend|st)|pay)ab|(?:paedoph|infant)i|twa(?:twaff|dd)|(?:despic|laugh)ab|chestic|(?:cumbub|damna|killa)b|(?:fuck|asss)ho|d(?:ickho|oy)|s(?:trang|hema|eni))le|(?:(?:(?:cluster\\-|(?:(?:cluster|bumble)|dumb))|skull)fu|(?:faggotc|birdl)o|fucksti|tit(?:ty)?fu|clitfu|ballsa|shitdi|bareba|assco|nutsa|pollo|redne|wetba|f\\*u|d[1l])ck|(?:dic(?:tatorshi|ksla)|nincompoo|buttercu|shitblim|goodpoo|f(?:uck\\-|ed)u|milkso|f(?:ucku|d)|tram|pur)p|(?:supremacis|terroris|leftis|devian|fascis|addic|midge|puppe|fago)ts|(?:islamophobi|m(?:otherfuckk|ierd)|p(?:edophili|unt)|thumbzill|c(?:ocksukk|ip)|(?:assfuk|coksuc)k|s(?:wastik|megm|hot)|(?:loli|buce)t|nambl|v(?:agin|iagr|14?gr|ulv)|dirs|gaz|dvd|yad)a|h(?:ypocritica|ardcore|o(?:ar|mo)?|ell|ure)|i(?:sl(?:amaphobe)?|gnoranc|mmoral|diots?|nsan|ll)|(?:extermin|castr)ate|(?:opportu|misogy)nist|i(?:ncompetent|gnoramu)|(?:ejaculatin|doucheba)g|(?:(?:fistfuck|beat)|kill)ing|(?:knuckle|fuck)head|(?:transgende|predato|offende|invade|(?:grift|monst|loos|hook|bean|deal)e|kille|nigg?e|lia)r|(?:supremacis|terroris|leftis|devian|fascis|addic|midge|puppe|fago)t|i(?:slam(?:ophobe|ic)|gnoran(?:ce|t)|dio(?:t(?:s'|a)|cy)|nsane|cky)|fingerfucks|homosexuals|narcissists|v(?:(?:(?:ollpfost|ix)e|ermi)n|a\\-j\\-j|ögeln)|hardcoresex|(?:dingleberr|trann|p(?:ant|\\*ss))y|(?:dicktick|cumguzz)ler|(?:crypto\\-?naz|omorash|futanar|nawash|shibar|henta|(?:mus|ec)ch|muft|rusk|wwii|pak)i|(?:coprolagn|n[iy]mphoman|lab|maf)ia|coprophilia|fingerfuck|homosexual|r(?:idiculous?|a(?:p(?:ist|e)|cis|t)|eich|ot)|narcissist|p(?:rostitute|sychopath|(?:edophil|arasit)e|isser|unk)|(?:degenerat|illiterat|sn(?:owfl)?ak|(?:butt|a(?:ss)?)hol|nippl|pub)e|x(?:enophobi[ac]|xx)|c(?:o(?:cks(?:u(?:ck(?:e[dr]|s)|ka)|moker)|wards|ochy|mmie|ke|x)|r(?:iminals|e(?:tins|ep[sy])|a(?:zy|w))|u(?:nnie|l[ot])|lits|hin[ck])|n(?:a(?:rcissism|zis|sty)|u(?:tt(?:en|y)|de)|igga[hsz]|1gga)|p(?:e(?:netrate[ds]|tulant|[eo])|o(?:o(?:nan[iy]|py|f)|rnos)|i(?:sse[ns]|gs)|u(?:nany|ss(?:y[hs]|e)|to)|h(?:u(?:ks|q)|ony)|\\*\\*\\*y|ron|af)|ejaculate[ds]|s(?:ociopaths|ycophants|h(?:i(?:t(?:faced|t(?:ed|y)|e[dy])|\\+)|oots|![+t]|\\*t|t)|t(?:upi(?:d(?:ism|er|a)|b)|ink[sy]|oned|fu|d)|hagging|uck(?:e(?:rs|d)|ing|[sy])|c(?:rote|a(?:ms|t))|e(?:x(?:ism|[oy])|d)|p(?:o(?:uts|ok)|ick|ac|d)|adis[mt]|ill(?:ie|y)|l(?:eazy|uts|ap)|m(?:ell[sy]|ut)|odomy|\\.o\\.b\\.|wine|\\*\\*[*t]|not|sw)|t(?:rumpsters|e(?:rr(?:orize|ibl[ey])|ez)|ittie[5s]|h(?:reats|ief)|1tt1e5|w(?:ats|it)|onto)|(?:schnac|pin)keln|(?:dominatri|g(?:ayse|oatc))x|(?:liesprop|d(?:ouche\\-f|(?:ooch|\\-)b|(?:irt)?b)|sl(?:eaze|ut)b|ballb|cuntr|fagb)ag|(?:atrocit|green|roosk|fatt|libb|hipp|ovar)ies|c(?:o(?:ck(?:s(?:moke|uck)?)?|w(?:ard)?|rrup|[kn])|yberfuck?|u(?:nt(?:lick|s)?|ms?|ck)|h(?:ick(?:en)?|eat)|l(?:owns?|it)|r(?:e(?:tin|ep)|ap)|0ck)|cockroach|ejaculate|s(?:t(?:upi(?:d(?:est|i(?:ty|s)|s)?)?|ink)|ociopath|ycophant|h(?:i(?:t(?:face|bag|e|s)?|z)|oot|\\*\\*|a[gm])|haggin|ex(?:ual)?|l(?:eaze|ut)|uck(?:er|in)?|c(?:rew|um?)|p(?:out|ic)|atan|ick|meg)|cockmunch|t(?:r(?:umpster|a(?:itor|ns?|sh)|ol)|it(?:ty?|s)?|w(?:ink|unt|at))|ludicrous|h(?:ypocrite|[ao]g)|xenophobe|(?:charlat|lesbi)an|f(?:i(?:stfucks|nk)|u(?:ck(?:tart|ed)|ks)|reak(?:in['g]|s)|ellate|ools|agg?s|\\*(?:\\*k|c\\*)|kn|'n|ck|g)|buttfucka|negligent|(?:gangbang|pervert|retard|crook)s|miserabl[ey]|(?:jig(?:g(?:er|a)|a)bo|p(?:inocchi|endej)|bastinad|strappad|fellati|n(?:igabo|egr)|g(?:ring|ayd|ur)|lmf?a|b(?:uck|oz)|d(?:eg|a)g|mo\\-f|guid)o|(?:voyeurwe|rimjo|scru|hee)b|qu(?:e(?:e(?:r(?:bait|hole)|f)|af)|im)|z(?:o(?:ophilia|mbies)|ealots?)|f(?:u(?:ck(?:in(?:gs?)?|ers?|a|s)?|[kx])|i(?:stfuck|ng)|reak(?:in)?|a(?:g(?:got)?|nny|ke|t)|o(?:ol|ul)?|\\*(?:\\*\\*|ck)|eck|k)?|chickens|d(?:i(?:c(?:tator|ks?)|sg(?:usti?|race)|mwit)|um(?:b(?:ed|a|s)?|my)|o(?:nkey|uche|pe)|amn|ead)|bea?stial|p(?:athetic?|e(?:cker|nis|do)|i(?:ss(?:ed)?|g)|sycho|o(?:rno?|o(?:n|p)?)|u(?:nch|ss[iy])|\\*\\*\\*\\*|huk)|gangbang|assmunch|dickfuck|fuckbutt|fucktard|b(?:astardo|itcher|oob)|imbecile|s(?:hit(?:t(?:ing|er)|head|ing)|cumbag|chmuck|exist|teal|icko|ack|lug)|t(?:esticle|roll|hug)|b(?:a(?:stards|rbaric)|u(?:mbler|tts)|eeotch|i(?:tch(?:es|y)|gots)|loody|o(?:llo[kx]|ors|ner)|ra[ty]s)|(?:f(?:uck(?:nu|wi)t|aggit)|crazies)t|g(?:enocide|oo(?:f[sy]|[kn])|ag|ra)|friggin'|fanatics|mediocre|execute[ds]|r(?:idicule|a(?:p(?:ists|e[ds])|cism|ts)|obs)|(?:(?:(?:bitch|dogg)|kick)|piss)ing|(?:phone|live|hot)sex|jailbait|(?:a(?:ssw|rse)h|bungh|c(?:orn|unt)h|rath)ole|cumshots|b(?:a(?:stard|ng)|i(?:got(?:ry)?|tch)|eeotc|u(?:ms?|tt)|low|oor|rat)|f(?:uck(?:nu|wi)t|aggit)|crazies|g(?:ay(?:fuck|s)?|o(?:d(?:\\-dam|damn)|of))|friggin|foolish|corrupt|dumbass|jackass|pervert|(?:(?:(?:bitch|dogg)|kick)|piss)in|(?:assfu|cra)ck|asslick|v(?:icious|oyeur|ag)|lunatic|jerkoff|buffoon|blowjob|n(?:eonazi|ut(?:ter|job))|orgasim|(?:bollo|pri)ck|cumshot|lucifer|d(?:u(?:m(?:mazz|ber)|h)|o(?:nkeys|lt)|amnit|evil|irty)|(?:(?:f\\*\\*\\*)?|(?:fuc|bat))\\*\\*\\*|j(?:a(?:ck[*a]\\*\\*|p)|erks|iz[mz])|o(?:rg(?:asms|y)|mg)|(?:kinbak|f\\-?yo|doof|c[ds])u|(?:(?:foot|hand)j|gayb|ren)ob|lucife|absurd|k(?:i(?:dnap|ck|ll)|acke|nob|um)|n(?:ympho|ut(?:te|s)?|igga|azi|ob)|orgasm|retard|mental|w(?:i(?:chse|mp)|eird|h(?:ack|ore)|ank)|kondum|racist|useles|w(?:hiner|acko)|l(?:o(?:ser[5s]|w\\-?iq|ony)|icks|esbo|azy)|a(?:ss(?:bag|”)|cist|\\*s|pd)|abuse[dr]|k(?:acken|nobed|i(?:(?:ck|ll)s|nky)|u(?:ms|nt)|ook)|(?:rimja|nsf|bb)w|cheeto|(?:filth|junk)y|l(?:o(?:ser|on)|ame)?|a(?:rs(?:ch|e)|nal|\\*\\*|ss)|f(?:elch|\\-\\-k)|balls|crook|rabid|filth|queer|c(?:hump|oon)|dweeb|(?:bimb|dild)o|horny|burns|y(?:iffy|aoi|uck)|fick|fcuk|lust|j(?:erk|an|iz)|d\\*ck|meth|darn|burn|fook|junk|dink|hoer|lone|puke|2g1c|MILF|God|hoe|lon|a55|ödp|xx)"+"\\b";
				Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(joke);

				if(!matcher.find() && joke.length() > 2){
					this.jokes.add(joke.substring(0, joke.length()-2));
				}
			}
		};
	}
	
	public String getNewJoke(){
		logger.debug("Fetching a new joke sentence");
		if(jokes.size()<=1){
			collectNewJokes(100);
		}
		return jokes.getAndRemove();
	}

}
