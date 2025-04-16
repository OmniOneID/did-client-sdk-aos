/*
 * Copyright 2025 OmniOne.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.omnione.did.sdk.core.zkp.other.util;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.datamodel.zkp.CredentialPrimaryPublicKey;
import org.omnione.did.sdk.utility.DataModels.DigestEnum;
import org.omnione.did.sdk.utility.DigestUtils;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class BigIntegerUtil {
    public static String createNonce1() {
        String nonce = "";

        try {
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            String randomNum = String.valueOf(prng.nextInt());

            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result = sha.digest(randomNum.getBytes());
            nonce = byteArrayToHex(result);
            WalletLogger.getInstance().d(nonce);
        } catch (Exception e) {
        }

        return nonce;
    }

    public static String createNonce2() {
        String nonce = "";

        try {
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            String randomNum = String.valueOf(prng.nextInt());
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result = sha.digest(randomNum.getBytes());
            nonce = hexEncode(result);
            WalletLogger.getInstance().d(nonce);

        } catch (Exception e) {
        }

        return nonce;
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for(final byte b: a) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    public static String hexEncode(byte[] aInput) {
        StringBuilder result = new StringBuilder();

        char[] digits = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

        for (int idx = 0; idx < aInput.length; ++idx) {
            byte b = aInput[idx];
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }

        return result.toString();
    }

    public static void main(String args[]) {
        for (int i = 0 ; i < 10 ; i++) {
            createNonce1();
        }
    }

    //TODO: random 생명주기 고려 필요
    private final SecureRandom random;

    //TODO: 왜 1000인지 의해 불능
    private static final int MAX_ITERATIONS = 1000;
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger THREE = BigInteger.valueOf(3);

    public BigIntegerUtil() {
        this.random = RandomUtil.getSecureRandom();
    }

    public static BigInteger generateX(BigInteger p, BigInteger q, SecureRandom random) throws WalletCoreException {
        return createRandomInRange(TWO, p.multiply(q).subtract(THREE), random).add(TWO);
    }

    public static BigInteger generateQR(BigInteger n, SecureRandom random) throws WalletCoreException {
        BigInteger temp = createRandomInRange(BigInteger.ZERO, n, random);
        return temp.multiply(temp).mod(n);
    }

    public static BigInteger createRandomInteger(int bitLength, SecureRandom random) {
        return new BigInteger(1, createRandom(bitLength, random));
    }

    public static BigInteger createRandomInRange(BigInteger min, BigInteger max, SecureRandom random) throws WalletCoreException {
        int cmp = min.compareTo(max);
        if (cmp >= 0) {
            if (cmp > 0) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_INVALID_PARAMETER, "'MIN' may not be greater than 'MAX'");
            }
            return min;
        }

        if (min.bitLength() > max.bitLength() / 2) {
            return createRandomInRange(BigInteger.ZERO, max.subtract(min), random).add(min);
        }

        for (int i = 0; i < MAX_ITERATIONS; ++i) {
            BigInteger x = createRandomBigInteger(max.bitLength(), random);
            if (x.compareTo(min) >= 0 && x.compareTo(max) <= 0) {
                return x;
            }
        }

        // fall back to a faster (restricted) method
        return createRandomBigInteger(max.subtract(min).bitLength() - 1, random).add(min);
    }

    public static BigInteger createRandomBigInteger(int bitLength, SecureRandom random) {
        return new BigInteger(1, createRandom(bitLength, random));
    }

    public static BigInteger createRandomPrime(BigInteger start, int intervalBits, int maxBits, SecureRandom random) {
        int certainty = maxBits <= 1024 ? 80 : (96 + 16 * ((maxBits) - 1) /1024);

        BigInteger rv = null;
        while (true) {

            byte[] base = createRandom(intervalBits, random);
            base[base.length - 1] |= 0x01;

            rv = start.add(new BigInteger(1, base));

            if (rv.bitLength() > maxBits)
                continue;

            if (rv.isProbablePrime(certainty) == true)
                break;
        }
        return rv;
    }

    private static byte[] createRandom(int bitLength, SecureRandom random) {

        int nBytes = (bitLength + 7) / 8;

        byte[] rv = new byte[nBytes];
        random.nextBytes(rv);

        // strip off any excess bits in the MSB
        int xBits = 8 * nBytes - bitLength;
        rv[0] &= (byte) (255 >>> xBits);

        return rv;
    }

    public static BigInteger fromBytes(byte[] bytes) throws WalletCoreException {
        try {
            return new BigInteger(1, bytes);
        } catch (Exception e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_BIG_NUMBER_FROM_BYTE_FAIL);
        }
    }

    public static byte[] asUnsignedByteArray(BigInteger value) throws WalletCoreException {
        try {
            byte[] bytes = value.toByteArray();

            if (bytes[0] == 0) {
                byte[] tmp = new byte[bytes.length - 1];
                System.arraycopy(bytes, 1, tmp, 0, tmp.length);
                return tmp;
            }
            return bytes;
        } catch (Exception e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_BIG_NUMBER_TO_BYTE_FAIL);
        }
    }

    public static BigInteger getHash(byte[] src) throws UtilityException {
        return new BigInteger(1, DigestUtils.getDigest(src, DigestEnum.DIGEST_ENUM.SHA_256));
    }

    public static BigInteger calc_teq(CredentialPrimaryPublicKey publicKey,
                                      BigInteger a_prime,
                                      BigInteger e,
                                      BigInteger v,
                                      Map<String, BigInteger> m_tilde,
                                      BigInteger m2_tilde,
                                      Set<String> unrevealed_attrs) throws WalletCoreException {

        // 2. Compute t-values
        // a_prime^e % p_pub_key.n
        BigInteger t = a_prime.modPow(e, publicKey.getN());

        BigInteger cur_r, cur_m;

        for (String key : unrevealed_attrs) {

            cur_r = publicKey.getR().get(key);
            cur_m = m_tilde.get(key);

            if (cur_r == null)
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "Value by key '{}' not found in pk.r");

            if (cur_m == null)
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "Value by key '{}' not found in m_tilde");

            // result = result * (cur_r^cur_m % p_pub_key.n) % p_pub_key.n
            t = t.multiply(cur_r.modPow(cur_m, publicKey.getN())).mod(publicKey.getN());
        }

        // result = result *(S^v %n)%n
        t = t.multiply(publicKey.getS().modPow(v, publicKey.getN())).mod(publicKey.getN());

        // result = result *(rctxt^m2_tilde%n)%n
        t = t.multiply(publicKey.getRctxt().modPow(m2_tilde, publicKey.getN())).mod(publicKey.getN());

        return t;
    }

// 검증
//    public static BigInteger calc_teq(PublicKey publicKey1,
//                                      BigInteger a_prime1,
//                                      BigInteger e1,
//                                      BigInteger v1,
//                                      Map<String, BigInteger> m_tilde1,
//                                      BigInteger m2_tilde1,
//                                      Set<String> unrevealed_attrs1) throws ZkpException {
//
//
//        BigInteger n = new BigInteger("103477756773321781470602257130908061956660706558155350276615355125616944893893399942371300476477092120665892538459090422888810389912049602706677015315473017668719291705022051801018494246942192717057622622701467522977131003454808661976774131147969160725556874260283295857410780109363793502417995193735421884293925039172911383215695498387519734812050292066716860348836891955429960972496243942112398415015378349600957954439097213347838158612230366951436571842465002480719644614655795523364697528773657182019745647476849735908482402696878678150641974927612651414434345300668928722290458191368152628699929705576090602520749");
//        BigInteger z = new BigInteger("32042390067374764398397013590644119733389906044136219804409734567112662761237032357772585085227244596522637663095875275242153918422569565826750104369884665680158735446491373646279368070424614022602183225480835557311405019546887662404380908047159032324118440456987317219780592756423253511146849248011892067897117272365001134304965861277003605159559250961184931546523126268014063519524046290527836791263505818710830587983517535324356089264173532875466788541446211361175336902500915193487396324170766176517262873622211609182055023926023256367898772949846440174643891909484516338161120413305094987558105658539997941156171");
//        BigInteger s = new BigInteger("29230872833039116163691256958210707630629353187535235356190244207252480041692351459874460191091754557956356240794882149481553877672469318189607845188920368210746205194677561920978279022208226211110539477564387786753470129925517598766129034911488989025306661475124118157155948432673619886685161388504594898232124408676695452962507772346366803368136466467513582917582356544775850570252330714469070957262283687624519806407466625579234474150377412291540607692039416749931651729563759164958389335751575974445192408644973782586994588677656512902895551854827274163840133721614700369279484268893716269963036652180162331226281");
//        Map<String, BigInteger> r = new HashMap<>();
//        r.put("last_name", new BigInteger("84120755121673281786039752302106370959086220217339740538280097708090247635190259230177372792997871569605316729257110382690561782764865112060452673855309553610866812727652475254375586752895693526462947547583392335655354926220624806232242340491067794049952637693405354055408569250861598812920466127344909531933311856874763094390026935861576537241778516589456422194386000501559803954507554626736063708735998576932610067561198578122217454586783059123575780486212508848503825921587744029113926758152965590574108065945652796070266755922972414800751504686771827833092552383081239960161549857894785851913602364190537051570331"));
//        r.put("salary", new BigInteger("21013616439953057851459836625041564209183609275353846766192145535039056152079200296409359950631376518832819483831914888800349942178370250460351109198095564110232559728904537954939021992864893153558220286929586279331039275078764655518666470559754313557667304595735661586015817924160688942318306616565135968206035241338197352959940256314688849963120411977624590287674322671920485455197993282987715735486434892001277664902076551579462417830591369506914986031280183771993307795695658137033953459655543743171438301288719634026403710888521187696574493571596181556075881842643102175092536735014852218319603197756309307465096"));
//        r.put("employee_status", new BigInteger("102574874199111672909776892628566193598699055294639940675323658450406157850072201100229114804398487187533027105602672021139770736849913787331513035086714389180967829033365545380473334935812639633841166870392231729938401521128918804877200789547796858059198361074286353139756556475242226334649247968403629223181400361505694171043105006074755503189215690983008238697339814285545196763487076036324047000294486012178564649835638633488140360467378248418153593884315462040379367862729010602621960847293258704897978351104849916066397205302672757385457880351310728203861788387724498344675213225556558089934678707622583281878590"));
//        r.put("first_name", new BigInteger("25323981277104386824971491183732563808738818819060566488328265747234963296849562595655810165070117211940073632696606119420601926141973490824513167214927334113053245183911842658908822048110831289782226077493221883190825917668847012557277737782605127695200377323795065995136117327368920620311711093167763176838428068627524415841759375345639410480129246034004225534636338662004682987180123910613170849225347765462587985062454638693787567706907091281041594644150649305566189513419611476664809354799633816720772948737766987341650668734547011826966631276524821174996603041975181650959248755746892553070005976335635096699043"));
//        r.put("experience", new BigInteger("81209143330054815232043864000450190297604663738648665730733744083850863364705291101503461697260604678420938279196644083166071575756267768751786042156004794645470531072114048072407556721380267166713392694618252163907058002832290298760305236502919410145548460699101296860367123591332941640361542364900568932027668464834649638774763133093331959774868498886081806893181616451063540713967335542651359549104605985480666155575756568103666066741006185041715220066579018740266091230468496312860661068007167446330158506673044467744137784516636211581150967389342470961308899043984886118262381856839358299590345813054745809039329"));
//        r.put("master_secret", new BigInteger("91028775489049260061913017493582940214407826938860206484576366933753609386992721821847119713878785937066977464698744724114736186246119310347034397861928990153683901570118933140268222842504181615584807634816350326877499618325323055980244583957949582638033290935220455490426732232895694012378893921352640776453467797746385723759731304291639076291364865051529451577079717189158180516460297449182757994250033513839858513363831811510066501864753523779338084772602931544400587268735204152109465182493965085846189937422413555730579737604430184640778643794725915854867939620004160112126271556961998886791416870364358048948227"));
//        BigInteger rctxt = new BigInteger("32971459084344953221422329890952022186176628534273207637630222918756390485765322309717811239465712325760697628637239485017796122144580796031330957138409611121813042569577535170140926940332232298243121970157460030243433746013127079486919393012212328126547576821046007471246865850543177351293607189666179296361770712505955180482168913305063484405052381940823136288709876793186910983977210053469985949165716596791670006089473403007730123379255237079259193129531421805799617425316987754076717608752504068458981808372640951736372678836458337327530895952569874456101766206580441754227767278379583075875451981396705752727906");
//        BigInteger a_prime = new BigInteger("101139292671716543753816951599590311920834147667365402229304760935298039450122746278746441227731880197696240573632484329817876075524020257692428375812214587287611677466250411695061299100362132098474844405184703019351884140266036511731954322620008765961263354063960991698069862901595341913008370344644262601837928255291103991232692372054319970444159379623373554408188672267475922250678197391841281672117410790662438798743494714430691001792561235365437863773149748341254672340167368675526988977043506627391311789916497395170233722389955550633959871524244873113574423748261335142315567616792544754544141758199955397501393");
//        BigInteger e = new BigInteger("158902792528180356790439675366657770198522909153754428046262237222473673367203648899865790706911082185284924319921215941390208847193164647");
//        BigInteger v = new BigInteger("280371062070570882850273111929408718840602886790587501007356419522701134474433896889260206663330504343433589220750225323955149917666087264333075968900528148464402554069237261010226771224818703079357401487137349970337714203464332181541391706399571479769714178053607554728725286284183655523453314978054874678070594247174459523018896875105677459223124660158456492081173087431313418685652131065386078989980444482099441841615299992426826264819930842434270730272207888814100773866481320932521935549454166519720827488465306384880759218044186388706977260184927415726101196397458100121987559027497359099882040739573222157461688220625799225616177328830125004306627690715283299171594156050887377991155719268169688412905311018017167890805900738824738314762780634682120368203616952645106144173213961309614021360491400712530230390479771151712158735425138904616895847115244057119311812043024550509986573682676233842634499603818653351646");
//
//        Map<String, BigInteger> m_tilde = new HashMap<>();
//        m_tilde.put("experience", new BigInteger("3711853002376907828151878050364320887388853395423832389819864948590356647048988355828104558074047289886092328208895305722808060303810041521836134531157432966577393416548864242631"));
//        m_tilde.put("first_name", new BigInteger("514341149772954452590634181799365993988883594407763272827512175189061351551537345055205154003123304900897288999817372742056784556201490500006084472546648891514461674774755974513"));
//        m_tilde.put("last_name", new BigInteger("19787180843539818307250486423548446685870962061508924144134816350934441838729762736221334189995782281851085318241980040239714721463258141802270694889943040046611033462819148877"));
//        m_tilde.put("master_secret", new BigInteger("15381939589860089910529569956085178509808005262111744647708968504368628833509046651172982701451784701503878807040992106469611912521326166494706185603236633260954266750014134704039"));
//        m_tilde.put("salary", new BigInteger("15298375788983779803922842626916986457900051026983371911407028603316765921483307452552882357420701407203114800687616293486881928869546793628842278975686068458648202325578412319116"));
//
//        BigInteger m2_tilde = new BigInteger("10095008831755370568921444978898660143420196689847622466264813368958828339478");
//
//        Set<String> unrevealed_attrs = new HashSet<>();
//        unrevealed_attrs.add("master_secret");
//        unrevealed_attrs.add("last_name");
//        unrevealed_attrs.add("experience");
//        unrevealed_attrs.add("salary");
//        unrevealed_attrs.add("first_name");
//
//
//        // 2. Compute t-values
//        // a_prime^e % p_pub_key.n
//        BigInteger result = a_prime.modPow(e, n);
//
//        WalletLogger.getInstance().d("m2_tilde: "+m2_tilde);
//
//        BigInteger cur_r, cur_m;
//
//        for (String key : unrevealed_attrs) {
//            cur_r = r.get(key);
//            cur_m = m_tilde.get(key);
//
//            if (cur_r == null)
//                throw new ZkpException(ErrorCode.NULL, "Value by key '{}' not found in pk.r ");
//
//            if (cur_m == null)
//                throw new ZkpException(ErrorCode.NULL, "Value by key '{}' not found in m_tilde ");
//
//            // result = result * (cur_r^cur_m % p_pub_key.n) % p_pub_key.n
//            result = result.multiply(cur_r.modPow(cur_m, n)).mod(n);
//        }
//
//        // result = result *(S^v %n)%n
//        result = result.multiply(s.modPow(v, n)).mod(n);
//
//        // result = result *(rctxt^m2_tilde%n)%n
//        result = result.multiply(rctxt.modPow(m2_tilde, n)).mod(n);
//
//        WalletLogger.getInstance().d("result: "+result);
//
//        return result;
//    }

    //TODO: 검증 필요
    public static Vector<BigInteger> calc_tne(CredentialPrimaryPublicKey p_pub_key,
                                              Map<String, BigInteger> u,
                                              Map<String, BigInteger> r,
                                              BigInteger mj,
                                              BigInteger alpha,
                                              Map<String, BigInteger> t,
                                              boolean is_less) throws WalletCoreException {

        Vector<BigInteger> t_list = new Vector<BigInteger>();
        for (int i = 0; i < ZkpConstants.ITERATION; i++) {
            BigInteger cur_u = u.get(Integer.toString(i));
            if (cur_u == null)
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "Value by key [" + i + "] not found in u");

            BigInteger cur_r = r.get(Integer.toString(i));
            if (cur_r == null)
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "Value by key [" + i + "] not found in r");

            BigInteger t_tau = p_pub_key.getZ().modPow(cur_u, p_pub_key.getN()).multiply(p_pub_key.getS().modPow(cur_r, p_pub_key.getN())).mod(p_pub_key.getN());
            // add this values to T in the order T1, T2, T3, T4, T∆.
            t_list.add(t_tau);
        }

        BigInteger delta = r.get(ZkpConstants.DELTA);
        if (delta == null)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "Value by key DELTA not found in r");

        BigInteger delta_predicate = (is_less == true) ? delta.negate() : delta;

        BigInteger t_tau = p_pub_key.getZ().modPow(mj, p_pub_key.getN()).multiply(p_pub_key.getS().modPow(delta_predicate, p_pub_key.getN())).mod(p_pub_key.getN());

        t_list.add(t_tau);

        BigInteger q = BigInteger.ONE;

        for (int i = 0; i < ZkpConstants.ITERATION; i++) {
            BigInteger cur_t = t.get(Integer.toString(i));
            if (cur_t == null)
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "Value by key [" + i + "] not found in t");

            BigInteger cur_u = u.get(Integer.toString(i));
            if (cur_u == null)
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "Value by key [" + i + "] not found in u");

            q = cur_t.modPow(cur_u, p_pub_key.getN()).multiply(q);
        }

        q = p_pub_key.getS().modPow(alpha, p_pub_key.getN()).multiply(q).mod(p_pub_key.getN());
        // Add q to t_list
        t_list.add(q);

        return t_list;
    }
    //TODO: 검증 필요
//    public static Vector<BigInteger> calc_tne(CredentialPrimaryPublicKey publicKey,
//                                              Map<String, BigInteger> u,
//                                              Map<String, BigInteger> r,
//                                              Map<String, BigInteger> t,
//                                              BigInteger mj,
//                                              BigInteger alpha,
//                                              boolean isLess) {
//        final BigInteger s = publicKey.getS();
//        final BigInteger z = publicKey.getZ();
//        final BigInteger n = publicKey.getN();
//
//        Vector<BigInteger> tList = new Vector<BigInteger>();
//
//        for (int i = 0; i < ZkpConstants.ITERATION; i++) {
//
//            String index = Integer.toString(i);
//
//            BigInteger cur_u = u.get(index);
//            //TODO: null check here
//            BigInteger cur_r = r.get(index);
//            //TODO: null check here
//
//            tList.add(CommitmentHelper.commitment(z, cur_u, s, cur_r, n));
//        }
//
//        BigInteger delta = r.get(ZkpConstants.DELTA);
//        //TODO: null check here
//
//        //TODO: isLess 모호함
//        BigInteger deltaPredicate = (isLess == true) ? delta.negate() : delta;
//
//        tList.add(CommitmentHelper.commitment(z, mj, s, deltaPredicate, n));
//
//        BigInteger q = BigInteger.ONE;
//        for (int i = 0; i < ZkpConstants.ITERATION; i++) {
//            String index = Integer.toString(i);
//            BigInteger cur_t = t.get(index);
//            //TODO: null check here
//
//            BigInteger cur_u = u.get(index);
//
//            q = cur_t.modPow(cur_u, n).multiply(q);
//        }
//        q = s.modPow(alpha, n).multiply(q).mod(n);
//        tList.add(q);
//
//        return tList;
//    }

    // TODO: bufferOverflow check..test value limit is 99999999
    /**
     * 모든 자연수에 대하여, 아래의 식을 만족하는 네 개의 음이 아닌 정수 a,b,c,d가 존재한다.
     * n = a^2 + b^2 + c^2 + d^2
     * */
    public static int[] four_squares(int delta) throws WalletCoreException {
        if (delta < 0)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_INVALID_PARAMETER, "Cannot express a negative number as sum of four squares" + delta);

        int[] roots = new int[]{(int) largest_square_less_than(delta), 0, 0, 0};

        for (int i = roots[0]; i > 0; i--) {
            roots[0] = i;
            if (delta == roots[0]) {
                roots[1] = roots[2] = roots[3] = 0;
                return roots;
            }
            roots[1] = largest_square_less_than(delta - (roots[0] * roots[0]));
            for (int j = roots[1]; j > 0; j--) {
                roots[1] = j;
                if (delta == ((roots[0] * roots[0]) + (roots[1] * roots[1]))) {
                    roots[2] = roots[3] = 0;
                    return roots;
                }
                roots[2] = largest_square_less_than(delta - ((roots[0] * roots[0]) + (roots[1] * roots[1])));
                for (int k = roots[2]; k > 0; k--) {
                    roots[2] = k;
                    int exptemp = (roots[0] * roots[0]) + (roots[1] * roots[1]) + (roots[2] * roots[2]);
                    if (delta == exptemp) {
                        roots[3] = 0;
                        return roots;
                    }
                    roots[3] = largest_square_less_than(delta - exptemp);
                    exptemp += (roots[3] * roots[3]);
                    if (delta == exptemp) {
                        return roots;
                    }

                }
            }
        }
        return roots;
    }
    private static int largest_square_less_than(int usize) {
        return (int) Math.floor(Math.sqrt((double) usize));
    }
    // ---------------

    public BigInteger generateX(BigInteger p, BigInteger q) throws WalletCoreException {
        return generateX(p, q, this.random);
    }
    public BigInteger generateQR(BigInteger n) throws WalletCoreException {
        return generateQR(n, this.random);
    }
    public BigInteger createRandomBigInteger(int bitLength) {
        return createRandomBigInteger(bitLength, this.random);
    }

    public BigInteger createRandom(BigInteger max) throws WalletCoreException {
        return createRandomInRange(BigInteger.ZERO, max, this.random);
    }
    public BigInteger createRandomPrime(BigInteger start, int intervalBits, int maxBits) {
        return createRandomPrime(start, intervalBits, maxBits, this.random);
    }
    // utils
    public SecureRandom getRandom() {
        return this.random;
    }

    public BigInteger generateNonce() {
        return createRandomBigInteger(ZkpConstants.LARGE_NONCE, this.random);
    }
    public BigInteger generateMasterSecret() throws WalletCoreException {
        try {
            return createRandomBigInteger(ZkpConstants.LARGE_MASTER_SECRET, this.random);
        } catch (Exception e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PROVER_GENERATE_MASTER_SECRET_FAIL);
        }
    }
}
