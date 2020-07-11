package me.felipe.wizly.rankup;

public class Rank {
	
	private String rank = "";
	private String display = "";
	private int custo = 0;
	private int position = 0;
	private String grupo = "";
	
	public Rank(String rank, int custo, String display, int position, String grupo){
		this.rank = rank;
		this.custo = custo;
		this.display = display;
		this.position = position;
		this.grupo = grupo;
	}
	
	public int getCusto(){
		return this.custo;
	}
	public String getGrupo(){
		return this.grupo;
	}
	public int getPosition(){
		return this.position;
	}
	public String getDisplayName(){
		return this.display;
	}
	public String getName(){
		return this.rank;
	}

}

