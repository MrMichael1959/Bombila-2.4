package com.bombila.michael.bombila;

import java.util.HashMap;
import java.util.Map;

public class _Sector {

    public static String getMySector(double lat, double lon) {
        Map<String,_IdLtLn> map = getSectorsInfo();
        String nm = "";
        _IdLtLn data = null;
        double prev = 1000000.0;
        for(Map.Entry<String,_IdLtLn> entry : map.entrySet()) {
            data = entry.getValue();
            double curr = getDistance(lat, lon, data.lat, data.lon);
            if(curr < prev) {
                prev = curr;
                nm = entry.getKey();
            }
        }
        return nm;
    }

    public static _IdLtLn[] getDirsLtLn(String dirs) {
        if(dirs.equals("")) return new _IdLtLn[]{};
        String arr[] = dirs.split(",");
        int size = arr.length;
        _IdLtLn dirsLtLn[] = new _IdLtLn[size];
        Map<String,_IdLtLn> sectors = getSectorsInfo();
        for (int i=0; i<size; i++) {
            dirsLtLn[i] = sectors.get(arr[i]);
        }
        return dirsLtLn;
    }

//--------------------------------------------------------------------------------------------------
    public static double getDistance(double s1, double d1, double s2, double d2) {
//--------------------------------------------------------------------------------------------------
        return 111.2 * Math.sqrt(Math.pow(s1-s2,2) + Math.pow((d1-d2)* Math.cos(Math.PI*s1/180),2));
    }

    public static Map<String,_IdLtLn> getSectorsInfo() {
        Map<String,_IdLtLn> sectors = new HashMap<String, _IdLtLn>();

        sectors.put("ПЯТИХАТКИ",new _IdLtLn(1606, 50.10440330587981,36.2586800424217));
        sectors.put("ЖУКИ",new _IdLtLn(168, 50.046664970665496,36.289998959720634));
        sectors.put("ПОБЕДЫ",new _IdLtLn(2518, 50.06808419590214,36.20255613117479));
        sectors.put("АЛЕКСЕЕВСКАЯ",new _IdLtLn(167, 50.04459449023865,36.200415936136324));
        sectors.put("ДЕРЕВЯНКО",new _IdLtLn(2519, 50.03439280442539,36.22927737131249));
        sectors.put("ПОЛЯ",new _IdLtLn(166, 50.03176957228907,36.20954032003783));
        sectors.put("ДИНАМОВСКАЯ",new _IdLtLn(2521, 50.016637203794346,36.235929249669425));
        sectors.put("КОСМИЧЕСКАЯ",new _IdLtLn(874, 50.01289714769689,36.21769370198672));
        sectors.put("ПУШКИНСКАЯ",new _IdLtLn(154, 50.00745159989441,36.2477382021375));
        sectors.put("СОВЕТСКАЯ",new _IdLtLn(160, 49.99436381486179,36.233775962890604));
        sectors.put("ВОКЗАЛ",new _IdLtLn(873, 49.99001848023688,36.21172850555217));
        sectors.put("АВТОВОКЗАЛ",new _IdLtLn(2040, 49.981739640801266,36.246402736287564));
        sectors.put("МЕТАЛЛИСТ",new _IdLtLn(159, 49.97379413408461,36.26488930557434));
        sectors.put("ПОС.АРТЕМА",new _IdLtLn(2516, 49.967440066793124,36.28700494766235));
        sectors.put("КИЕВСКАЯ",new _IdLtLn(2515, 50.00684676048695,36.27468180551659));
        sectors.put("ГИДРОПАРК",new _IdLtLn(1438, 50.02500422249629,36.294408898159645));
        sectors.put("СОРТИРОВКА",new _IdLtLn(1419, 50.03175604407764,36.17109805056465));
        sectors.put("ЛЫС.ГОРА",new _IdLtLn(187, 50.000237014401016,36.17542501309114));
        sectors.put("ЗАЛЮТИНО",new _IdLtLn(1588, 49.980564,36.140205));
        sectors.put("ХОЛ ГОРА",new _IdLtLn(156, 49.97666412764575,36.1822730634766));
        sectors.put("БАВАРИЯ",new _IdLtLn(161, 49.94816700745697,36.15447308126329));
        sectors.put("Б.ДАНИЛОВКА",new _IdLtLn(1607, 50.046703967548176,36.321096969112894));
        sectors.put("С.САЛТ.",new _IdLtLn(164, 50.03855600748694,36.35468298978516));
        sectors.put("ВОСТ.САЛТ",new _IdLtLn(162, 50.01238478966565,36.35823313778417));
        sectors.put("СТУДНЯК",new _IdLtLn(668, 50.01899097762624,36.32620800523992));
        sectors.put("602-й",new _IdLtLn(1420, 49.99015406274687,36.35639910861369));
        sectors.put("Ц.САЛТОВКА",new _IdLtLn(2522, 50.001936998686325,36.330128431582125));
        sectors.put("ЮГ САЛТ.",new _IdLtLn(163, 49.98766004616975,36.32746565751597));
        sectors.put("ТРК ФРАНЦ.БУЛЬВ.",new _IdLtLn(2517, 49.9913986259126,36.30266260995995));
        sectors.put("ЯКИРА",new _IdLtLn(1430, 49.997985,36.293724));
        sectors.put("НЕМЫШЛЯ",new _IdLtLn(2041, 49.97100914803476,36.350978849222884));
        sectors.put("ЖУКОВА",new _IdLtLn(1433, 49.962130539008264,36.316754156144725));
        sectors.put("НОВ.ДОМА",new _IdLtLn(155, 49.95290807517752,36.34586740315058));
        sectors.put("КОММУН.РЫНОК",new _IdLtLn(2060, 49.94544835589309,36.31002902984619));
        sectors.put("ХТЗ",new _IdLtLn(157, 49.92844663589327,36.372481162918916));
        sectors.put("РОГАНЬ",new _IdLtLn(2045, 49.914734277169934,36.417480470845476));
        sectors.put("ГОРИЗОНТ",new _IdLtLn(2514, 49.92702446845727,36.45220756530762));
        sectors.put("ДОКУЧАЕВА",new _IdLtLn(2043, 49.881329373192024,36.43526458792621));
        sectors.put("АЭРОПОРТ",new _IdLtLn(1439, 49.916490473030755,36.2681672698975));
        sectors.put("ОДЕССКАЯ",new _IdLtLn(2525, 49.94287762926473,36.24708723917138));
        sectors.put("ЗЕРНОВАЯ",new _IdLtLn(158, 49.94893968089093,36.27532553227752));
        sectors.put("ДИКАНЕВКА",new _IdLtLn(2042, 49.96399770844891,36.240085599711165));
        sectors.put("МОСКАЛЕВКА",new _IdLtLn(1432, 49.97497407911293,36.22711829877835));
        sectors.put("НОВОЖАНОВО",new _IdLtLn(2520, 49.963404173566424,36.216374872019514));
        sectors.put("ЖИХАРЬ",new _IdLtLn(2387, 49.91276667304493,36.23119354248047));
        sectors.put("ФИЛИППОВКА",new _IdLtLn(2523, 49.9343433061111,36.192970990086906));
        sectors.put("БЕЗЛЮДОВКА",new _IdLtLn(1440, 49.864507640049546,36.277741957177795));
        sectors.put("ПЛЯЖ АЛЕКСАНДРА",new _IdLtLn(2541, 49.882595885759905,36.25356959877536));
        sectors.put("БАБАИ",new _IdLtLn(2524, 49.90002319475268,36.19043898477685));
        sectors.put("ВЫСОКИЙ",new _IdLtLn(2039, 49.88324295662375,36.13420486450195));
        sectors.put("ПЕСОЧИН",new _IdLtLn(1879, 49.95661342947703,36.08725547790527));
        sectors.put("М.ДАНИЛОВКА",new _IdLtLn(1881, 50.07275858688542,36.15952491760254));
        sectors.put("КУЛИНИЧИ",new _IdLtLn(1880, 49.98285450402907,36.38663291931152));
        sectors.put("СОЛОНИЦЕВКА",new _IdLtLn(2470, 49.99449830610663,36.04957580566406));
        sectors.put("ЦИРКУНЫ",new _IdLtLn(2542, 50.09305398404301,36.36474609375));

        return sectors;
    }
}
