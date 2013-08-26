package fr.meijin.cataclysm.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public abstract class ReferentielMission {
	
	public static List<String> getMissions(String faction){
		List<String> missions = new ArrayList<String>();
		
		missions.addAll(GENERAL.getAll());
		
		if (StringUtils.equals(faction, "askari"))
			missions.addAll(ASKARI.getAll());
		else if (StringUtils.equals(faction, "bamakas"))
			missions.addAll(BAMAKAS.getAll());
		else if (StringUtils.equals(faction, "convoi"))
			missions.addAll(CONVOI.getAll());
		else if (StringUtils.equals(faction, "horde"))
			missions.addAll(HORDE.getAll());
		else if (StringUtils.equals(faction, "isc"))
			missions.addAll(ISC.getAll());
		else if (StringUtils.equals(faction, "jokers"))
			missions.addAll(JOKERS.getAll());
		else if (StringUtils.equals(faction, "matriarcat"))
			missions.addAll(MATRIARCAT.getAll());
		else if (StringUtils.equals(faction, "resistance"))
			missions.addAll(RESISTANCE.getAll());
		
		return missions;
	}

	public static abstract class GENERAL {
		public static final String LA_SOURCE_PURE = "La source pure";
		public static final String PROTECTION_DU_TERRITOIRE = "Protection du territoire";
		public static final String LE_TRAITRE = "Le traitre";
		public static final String EXPEDITION_PUNITIVE = "Expédition punitive";
		public static final String AUX_QUATRE_VENTS = "Aux quatre vents";
		public static final String CIBLE_PRIORITAIRE = "Cible prioritaire";
		public static final String GUET_APENS = "Guet-apens";
		public static final String LE_TRAQUENARD = "Le traquenard";
		public static final String LES_ARTEFACTS_ANCIENS = "Les artefacts anciens";
		public static final String SURVIE_EN_MILIEU_HOSTILE = "Survie en milieu hostile";
		public static final String LES_TOURELLES_DGIN = "Les tourelles D.G.I.N";
		public static final String COMBAT_DES_CHEFS = "Combat des chefs";
		public static final String EXPLORATION = "Exploration";
		
		public static List<String> getAll(){
			List<String> missions = new ArrayList<String>();
			
			missions.add(LA_SOURCE_PURE);
			missions.add(PROTECTION_DU_TERRITOIRE);
			missions.add(LE_TRAITRE);
			missions.add(EXPEDITION_PUNITIVE);
			missions.add(AUX_QUATRE_VENTS);
			missions.add(CIBLE_PRIORITAIRE);
			missions.add(GUET_APENS);
			missions.add(LE_TRAQUENARD);
			missions.add(LES_ARTEFACTS_ANCIENS);
			missions.add(SURVIE_EN_MILIEU_HOSTILE);
			missions.add(LES_TOURELLES_DGIN);
			missions.add(COMBAT_DES_CHEFS);
			missions.add(EXPLORATION);
			
			return missions;
		}
	}

	public static abstract class ASKARI {
		public static final String RECOLTE_GENOMIQUE = "Récolte génomique";
		public static final String CONTAGION = "Contagion";
		public static final String L_EVADE = "L'évadé";
		
		public static List<String> getAll(){
			List<String> missions = new ArrayList<String>();
			
			missions.add(RECOLTE_GENOMIQUE);
			missions.add(CONTAGION);
			missions.add(L_EVADE);
			
			return missions;
		}
	}

	public static abstract class BAMAKAS {
		public static final String REPLI_VERS_LES_ZONES_CONTAMINEES = "Repli vers les zones contaminées";
		public static final String LA_CHASSE_SACREE = "La chasse sacrée";
		public static final String LA_CEREMONIE_FUNERAIRE = "La cérémonie funéraire";
		
		public static List<String> getAll(){
			List<String> missions = new ArrayList<String>();
			
			missions.add(REPLI_VERS_LES_ZONES_CONTAMINEES);
			missions.add(LA_CHASSE_SACREE);
			missions.add(LA_CEREMONIE_FUNERAIRE);
			
			return missions;
		}
	}

	public static abstract class CONVOI {
		public static final String INFILTRATION = "Infiltration";
		public static final String LE_CONTAINER = "Le container";
		public static final String LE_BOEUF = "Le boeuf";
		
		public static List<String> getAll(){
			List<String> missions = new ArrayList<String>();
			
			missions.add(INFILTRATION);
			missions.add(LE_CONTAINER);
			missions.add(LE_BOEUF);
			
			return missions;
		}
	}

	public static abstract class HORDE {
		public static final String LES_NECROPHAGES = "Les nécrophages";
		public static final String IMPREVU = "Imprévu";
		
		public static List<String> getAll(){
			List<String> missions = new ArrayList<String>();
			
			missions.add(LES_NECROPHAGES);
			missions.add(IMPREVU);
			
			return missions;
		}
	}

	public static abstract class ISC {
		public static final String RECONNAISSANCE = "Reconnaissance";
		public static final String CAPTURE = "Capture";
		public static final String LES_FOUILLES = "Les fouilles";
		
		public static List<String> getAll(){
			List<String> missions = new ArrayList<String>();
			
			missions.add(RECONNAISSANCE);
			missions.add(CAPTURE);
			missions.add(LES_FOUILLES);
			
			return missions;
		}
	}

	public static abstract class JOKERS {
		public static final String PILLAGE = "Pillage";
		public static final String LA_CAME = "La came";
		public static final String LE_TERRITOIRE = "Le territoire";
		
		public static List<String> getAll(){
			List<String> missions = new ArrayList<String>();
			
			missions.add(PILLAGE);
			missions.add(LA_CAME);
			missions.add(LE_TERRITOIRE);
			
			return missions;
		}
	}

	public static abstract class MATRIARCAT {
		public static final String GARDE_RAPPROCHEE = "Garde rapprochée";
		public static final String LES_IDOLES_IMPIES = "Les idoles impies";
		public static final String LES_SAINTES_RELIQUES = "Les saintes reliques";
		
		public static List<String> getAll(){
			List<String> missions = new ArrayList<String>();
			
			missions.add(GARDE_RAPPROCHEE);
			missions.add(LES_IDOLES_IMPIES);
			missions.add(LES_SAINTES_RELIQUES);
			
			return missions;
		}
	}

	public static abstract class RESISTANCE {
		public static final String LES_EOLIENNES = "Les éoliennes";
		public static final String SUIVRE_LA_PISTE = "Suivre la piste";
		public static final String L_ABRI_SOUTERRAIN = "L'abri souterrain";
		
		public static List<String> getAll(){
			List<String> missions = new ArrayList<String>();
			
			missions.add(LES_EOLIENNES);
			missions.add(SUIVRE_LA_PISTE);
			missions.add(L_ABRI_SOUTERRAIN);
			
			return missions;
		}
	}
}
