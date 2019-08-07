/********************************************************************************************************************************************************************************************************************************************************************
 *                                                                                                                                                                                                                                                                  *
 *                                                                                                                                                                                                                                                                  *
 *        Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)                                                                                                                                                                                                   *
 *                                                                                                                                                                                                                                                                  *
 *        This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.    *
 *        This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.           *
 *        You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.                                                                                                            *
 *                                                                                                                                                                                                                                                                  *
 ********************************************************************************************************************************************************************************************************************************************************************/
static struct {
	unsigned long	code;
	const char	*entity;
	int		len;
}	entity_map[] = {
	{ 34, "&quot;", 6 },
	{ 38, "&amp;", 5 },
	{ 39, "&apos;", 6 },
	{ 60, "&lt;", 4 },
	{ 62, "&gt;", 4 },
	{ 49824, "&nbsp;", 6 },
	{ 49825, "&iexcl;", 7 },
	{ 49826, "&cent;", 6 },
	{ 49827, "&pound;", 7 },
	{ 49828, "&curren;", 8 },
	{ 49829, "&yen;", 5 },
	{ 49830, "&brvbar;", 8 },
	{ 49831, "&sect;", 6 },
	{ 49832, "&uml;", 5 },
	{ 49833, "&copy;", 6 },
	{ 49834, "&ordf;", 6 },
	{ 49835, "&laquo;", 7 },
	{ 49836, "&not;", 5 },
	{ 49837, "&shy;", 5 },
	{ 49838, "&reg;", 5 },
	{ 49839, "&macr;", 6 },
	{ 49840, "&deg;", 5 },
	{ 49841, "&plusmn;", 8 },
	{ 49842, "&sup2;", 6 },
	{ 49843, "&sup3;", 6 },
	{ 49844, "&acute;", 7 },
	{ 49845, "&micro;", 7 },
	{ 49846, "&para;", 6 },
	{ 49847, "&middot;", 8 },
	{ 49848, "&cedil;", 7 },
	{ 49849, "&sup1;", 6 },
	{ 49850, "&ordm;", 6 },
	{ 49851, "&raquo;", 7 },
	{ 49852, "&frac14;", 8 },
	{ 49853, "&frac12;", 8 },
	{ 49854, "&frac34;", 8 },
	{ 49855, "&iquest;", 8 },
	{ 50048, "&Agrave;", 8 },
	{ 50049, "&Aacute;", 8 },
	{ 50050, "&Acirc;", 7 },
	{ 50051, "&Atilde;", 8 },
	{ 50052, "&Auml;", 6 },
	{ 50053, "&Aring;", 7 },
	{ 50054, "&AElig;", 7 },
	{ 50055, "&Ccedil;", 8 },
	{ 50056, "&Egrave;", 8 },
	{ 50057, "&Eacute;", 8 },
	{ 50058, "&Ecirc;", 7 },
	{ 50059, "&Euml;", 6 },
	{ 50060, "&Igrave;", 8 },
	{ 50061, "&Iacute;", 8 },
	{ 50062, "&Icirc;", 7 },
	{ 50063, "&Iuml;", 6 },
	{ 50064, "&ETH;", 5 },
	{ 50065, "&Ntilde;", 8 },
	{ 50066, "&Ograve;", 8 },
	{ 50067, "&Oacute;", 8 },
	{ 50068, "&Ocirc;", 7 },
	{ 50069, "&Otilde;", 8 },
	{ 50070, "&Ouml;", 6 },
	{ 50071, "&times;", 7 },
	{ 50072, "&Oslash;", 8 },
	{ 50073, "&Ugrave;", 8 },
	{ 50074, "&Uacute;", 8 },
	{ 50075, "&Ucirc;", 7 },
	{ 50076, "&Uuml;", 6 },
	{ 50077, "&Yacute;", 8 },
	{ 50078, "&THORN;", 7 },
	{ 50079, "&szlig;", 7 },
	{ 50080, "&agrave;", 8 },
	{ 50081, "&aacute;", 8 },
	{ 50082, "&acirc;", 7 },
	{ 50083, "&atilde;", 8 },
	{ 50084, "&auml;", 6 },
	{ 50085, "&aring;", 7 },
	{ 50086, "&aelig;", 7 },
	{ 50087, "&ccedil;", 8 },
	{ 50088, "&egrave;", 8 },
	{ 50089, "&eacute;", 8 },
	{ 50090, "&ecirc;", 7 },
	{ 50091, "&euml;", 6 },
	{ 50092, "&igrave;", 8 },
	{ 50093, "&iacute;", 8 },
	{ 50094, "&icirc;", 7 },
	{ 50095, "&iuml;", 6 },
	{ 50096, "&eth;", 5 },
	{ 50097, "&ntilde;", 8 },
	{ 50098, "&ograve;", 8 },
	{ 50099, "&oacute;", 8 },
	{ 50100, "&ocirc;", 7 },
	{ 50101, "&otilde;", 8 },
	{ 50102, "&ouml;", 6 },
	{ 50103, "&divide;", 8 },
	{ 50104, "&oslash;", 8 },
	{ 50105, "&ugrave;", 8 },
	{ 50106, "&uacute;", 8 },
	{ 50107, "&ucirc;", 7 },
	{ 50108, "&uuml;", 6 },
	{ 50109, "&yacute;", 8 },
	{ 50110, "&thorn;", 7 },
	{ 50111, "&yuml;", 6 },
	{ 50578, "&OElig;", 7 },
	{ 50579, "&oelig;", 7 },
	{ 50592, "&Scaron;", 8 },
	{ 50593, "&scaron;", 8 },
	{ 50616, "&Yuml;", 6 },
	{ 50834, "&fnof;", 6 },
	{ 52102, "&circ;", 6 },
	{ 52124, "&tilde;", 7 },
	{ 52881, "&Alpha;", 7 },
	{ 52882, "&Beta;", 6 },
	{ 52883, "&Gamma;", 7 },
	{ 52884, "&Delta;", 7 },
	{ 52885, "&Epsilon;", 9 },
	{ 52886, "&Zeta;", 6 },
	{ 52887, "&Eta;", 5 },
	{ 52888, "&Theta;", 7 },
	{ 52889, "&Iota;", 6 },
	{ 52890, "&Kappa;", 7 },
	{ 52891, "&Lambda;", 8 },
	{ 52892, "&Mu;", 4 },
	{ 52893, "&Nu;", 4 },
	{ 52894, "&Xi;", 4 },
	{ 52895, "&Omicron;", 9 },
	{ 52896, "&Pi;", 4 },
	{ 52897, "&Rho;", 5 },
	{ 52899, "&Sigma;", 7 },
	{ 52900, "&Tau;", 5 },
	{ 52901, "&Upsilon;", 9 },
	{ 52902, "&Phi;", 5 },
	{ 52903, "&Chi;", 5 },
	{ 52904, "&Psi;", 5 },
	{ 52905, "&Omega;", 7 },
	{ 52913, "&alpha;", 7 },
	{ 52914, "&beta;", 6 },
	{ 52915, "&gamma;", 7 },
	{ 52916, "&delta;", 7 },
	{ 52917, "&epsilon;", 9 },
	{ 52918, "&zeta;", 6 },
	{ 52919, "&eta;", 5 },
	{ 52920, "&theta;", 7 },
	{ 52921, "&iota;", 6 },
	{ 52922, "&kappa;", 7 },
	{ 52923, "&lambda;", 8 },
	{ 52924, "&mu;", 4 },
	{ 52925, "&nu;", 4 },
	{ 52926, "&xi;", 4 },
	{ 52927, "&omicron;", 9 },
	{ 53120, "&pi;", 4 },
	{ 53121, "&rho;", 5 },
	{ 53122, "&sigmaf;", 8 },
	{ 53123, "&sigma;", 7 },
	{ 53124, "&tau;", 5 },
	{ 53125, "&upsilon;", 9 },
	{ 53126, "&phi;", 5 },
	{ 53127, "&chi;", 5 },
	{ 53128, "&psi;", 5 },
	{ 53129, "&omega;", 7 },
	{ 53137, "&thetasym;", 10 },
	{ 53138, "&upsih;", 7 },
	{ 53142, "&piv;", 5 },
	{ 14844034, "&ensp;", 6 },
	{ 14844035, "&emsp;", 6 },
	{ 14844041, "&thinsp;", 8 },
	{ 14844044, "&zwnj;", 6 },
	{ 14844045, "&zwj;", 5 },
	{ 14844046, "&lrm;", 5 },
	{ 14844047, "&rlm;", 5 },
	{ 14844051, "&ndash;", 7 },
	{ 14844052, "&mdash;", 7 },
	{ 14844056, "&lsquo;", 7 },
	{ 14844057, "&rsquo;", 7 },
	{ 14844058, "&sbquo;", 7 },
	{ 14844060, "&ldquo;", 7 },
	{ 14844061, "&rdquo;", 7 },
	{ 14844062, "&bdquo;", 7 },
	{ 14844064, "&dagger;", 8 },
	{ 14844065, "&Dagger;", 8 },
	{ 14844066, "&bull;", 6 },
	{ 14844070, "&hellip;", 8 },
	{ 14844080, "&permil;", 8 },
	{ 14844082, "&prime;", 7 },
	{ 14844083, "&Prime;", 7 },
	{ 14844089, "&lsaquo;", 8 },
	{ 14844090, "&rsaquo;", 8 },
	{ 14844094, "&oline;", 7 },
	{ 14844292, "&frasl;", 7 },
	{ 14844588, "&euro;", 6 },
	{ 14845073, "&image;", 7 },
	{ 14845080, "&weierp;", 8 },
	{ 14845084, "&real;", 6 },
	{ 14845090, "&trade;", 7 },
	{ 14845109, "&alefsym;", 9 },
	{ 14845584, "&larr;", 6 },
	{ 14845585, "&uarr;", 6 },
	{ 14845586, "&rarr;", 6 },
	{ 14845587, "&darr;", 6 },
	{ 14845588, "&harr;", 6 },
	{ 14845621, "&crarr;", 7 },
	{ 14845840, "&lArr;", 6 },
	{ 14845841, "&uArr;", 6 },
	{ 14845842, "&rArr;", 6 },
	{ 14845843, "&dArr;", 6 },
	{ 14845844, "&hArr;", 6 },
	{ 14846080, "&forall;", 8 },
	{ 14846082, "&part;", 6 },
	{ 14846083, "&exist;", 7 },
	{ 14846085, "&empty;", 7 },
	{ 14846087, "&nabla;", 7 },
	{ 14846088, "&isin;", 6 },
	{ 14846089, "&notin;", 7 },
	{ 14846091, "&ni;", 4 },
	{ 14846095, "&prod;", 6 },
	{ 14846097, "&sum;", 5 },
	{ 14846098, "&minus;", 7 },
	{ 14846103, "&lowast;", 8 },
	{ 14846106, "&radic;", 7 },
	{ 14846109, "&prop;", 6 },
	{ 14846110, "&infin;", 7 },
	{ 14846112, "&ang;", 5 },
	{ 14846119, "&and;", 5 },
	{ 14846120, "&or;", 4 },
	{ 14846121, "&cap;", 5 },
	{ 14846122, "&cup;", 5 },
	{ 14846123, "&int;", 5 },
	{ 14846132, "&there4;", 8 },
	{ 14846140, "&sim;", 5 },
	{ 14846341, "&cong;", 6 },
	{ 14846344, "&asymp;", 7 },
	{ 14846368, "&ne;", 4 },
	{ 14846369, "&equiv;", 7 },
	{ 14846372, "&le;", 4 },
	{ 14846373, "&ge;", 4 },
	{ 14846594, "&sub;", 5 },
	{ 14846595, "&sup;", 5 },
	{ 14846596, "&nsub;", 6 },
	{ 14846598, "&sube;", 6 },
	{ 14846599, "&supe;", 6 },
	{ 14846613, "&oplus;", 7 },
	{ 14846615, "&otimes;", 8 },
	{ 14846629, "&perp;", 6 },
	{ 14846853, "&sdot;", 6 },
	{ 14847112, "&lceil;", 7 },
	{ 14847113, "&rceil;", 7 },
	{ 14847114, "&lfloor;", 8 },
	{ 14847115, "&rfloor;", 8 },
	{ 14847145, "&lang;", 6 },
	{ 14847146, "&rang;", 6 },
	{ 14849930, "&loz;", 5 },
	{ 14850464, "&spades;", 8 },
	{ 14850467, "&clubs;", 7 },
	{ 14850469, "&hearts;", 8 },
	{ 14850470, "&diams;", 7 },
};
