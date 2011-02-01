package com.coremedia.util.hudson.matrixBuild;

import com.coremedia.util.hudson.Report.StoryContainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class BacklogTestLoggerMatrixSubBuild implements Comparable<BacklogTestLoggerMatrixSubBuild> {

	/**
	 * List of axes value for this subBuild
	 */
	private Map<String, String> combinations;
	
	private StoryContainer report;
	
	public BacklogTestLoggerMatrixSubBuild(Map<String, String> comb, StoryContainer report) {
		this.combinations = comb;
		this.report = report;
	}

	public boolean hasCombination(Map<String, String> combination) {
		Set<Entry<String, String>> entry = this.combinations.entrySet();
		Iterator<Entry<String, String>>  iterator = entry.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> axe = iterator.next();
			if (combination.containsKey(axe.getKey()) && combination.get(axe.getKey()).equals(axe.getValue())) {
				return true;
			}			
		}
		return false;		
	}

	public Map<String, String> getCombinations() {
		return combinations;
	}
	
	public String getStringCombinations() {
		StringBuilder strb = new StringBuilder();
		
		Set<Entry<String, String>> entry = this.combinations.entrySet();
		Iterator<Entry<String, String>>  iterator = entry.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> axe = iterator.next();
			strb.append(axe.getKey()+"="+axe.getValue()+" ; ");		
		}
		
		return strb.toString();
	}

	
	
	public StoryContainer getReport() {
		return report;
	}

	public List<String> getAxis() {
		List<String> result = new ArrayList<String>();
		Set<String> axis = this.combinations.keySet();
		Object[] arr = (Object[]) axis.toArray();
		for (int i=0; i<arr.length; i++) {
			result.add((String)arr[i]);
		}
		return result;
	}

	public List<String> getAxisValues(String axe) {
		List<String> result = new ArrayList<String>();
		Set<Entry<String, String>> entry = this.combinations.entrySet();
		Iterator<Entry<String, String>>  iterator = entry.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> a = iterator.next();
			if (a.getKey().equals(axe) && !result.contains(a.getValue())) {
				result.add(a.getValue());
			}
		}
		return result;
	}
	/**
	 * Comparable function which offers a sorting solution
	 * 
	 */
	public int compareTo(BacklogTestLoggerMatrixSubBuild subBuild) {
		return this.getStringCombinations().compareTo(subBuild.getStringCombinations());		
	}
	
	
	
}
