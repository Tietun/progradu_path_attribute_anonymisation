package datagenerator;

/**
 * Contains static lists of cumulative age distributions per sex
 * @author Erkka Nurmi
 *
 */
public class AgeDistributions {

	/**
	 * Finnish cumulative age distributions per sex
	 * Sourced from findikaattori.fi, updated 31.12.2019.
	 * https://findikaattori.fi/fi/14#:~:text=Tilastokeskuksen%20v%C3%A4est%C3%B6rakennetilaston%20mukaan%20Suomessa%20oli,kolmessa%20vuodessa%20100%20000%20henkil%C3%B6ll%C3%A4
	 */
	public static final AgeDistribution FINLAND_CURRENT = new AgeDistribution(
			new int[]{22496,45711,70810,97439,125040,153918,183122,213332,243780,274673,305496,335919,365938,396155,425786,455218,
					484330,512666,541454,570227,599538,628544,658881,689973,722492,756276,790147,825132,859836,895085,929639,964265,
					997132,1030429,1064738,1100054,1136036,1171362,1205579,1239398,1273103,1306624,1340686,1374709,1407945,1439967,
					1468907,1498862,1530111,1562336,1595009,1630251,1666274,1702517,1738948,1776017,1813450,1850388,1887288,1924100,
					1960584,1995880,2032584,2070204,2107436,2145093,2181750,2219459,2255720,2293121,2331506,2370484,2408995,2446326,
					2479155,2505417,2530040,2549623,2577727,2597345,2619939,2640782,2659859,2677143,2693601,2708585,2721629,2734347,
					2745921,2756734,2766028,2773628,2779683,2784458,2788386,2791187,2793286,2794725,2795659,2796274},
			new int[]{23250,48125,74297,101782,130884,161074,191957,223389,255062,287538,319658,351450,382996,414549,445250,476290,
					506488,536391,566542,597480,628965,659755,691629,724767,759159,794691,830388,867732,904710,942251,979010,1015818,
					1051020,1086690,1123366,1161011,1199252,1237363,1273507,1309488,1344839,1380452,1416163,1452136,1487416,1520670,
					1551218,1582670,1614559,1647398,1681064,1716792,1753340,1789959,1826580,1863771,1901159,1938101,1974461,2010299,
					2045822,2079979,2115373,2151338,2186582,2221098,2255167,2290188,2323698,2357857,2392551,2427262,2461159,2493320,
					2521554,2543282,2563473,2579072,2600886,2615541,2631530,2646324,2659105,2670296,2680681,2689513,2696919,2703547,
					2709488,2714414,2718180,2721274,2723424,2725045,2726203,2727038,2727555,2727819,2727999,2728110});
	

}
