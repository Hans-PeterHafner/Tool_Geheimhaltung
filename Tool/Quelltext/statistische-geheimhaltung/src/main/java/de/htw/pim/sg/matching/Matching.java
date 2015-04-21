/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.pim.sg.matching;

import com.google.common.collect.ArrayTable;
import de.htw.pim.sg.util.ColumnType;
import de.htw.pim.sg.util.MetaColumn;
import de.htw.pim.sg.util.MetaData;
import de.htw.pim.sg.util.DistanceElement;
import java.util.ArrayList;
import java.util.Collections;
/**
 *
 * @author Michael Schidlauske
 */
//TODO implements MatchingInterface
public class Matching implements MatchingInterface{
    
    private double real_matches = 0;
    private double possible_matches = 0;
     
    /** Startet das Matching
     * 
     * @throws Exception 
     */
    public void startMatching(ArrayTable<Integer, Integer, String> original,
                    ArrayTable<Integer, Integer, String> anonym,
                    MetaData metaData) throws Exception
    {
        double[][] distances;
        //Gewichte standardisieren
        standardizeWeights(metaData);
        
        //Ordinalen Variablen wird numerisches Gewicht zugeordnet
        sortOrdinalVariables(original, metaData);
        sortOrdinalVariables(anonym, metaData);
        
        //Distanzen berechnen
        distances = calculateDistances(original, anonym, metaData);
        
        // Matching
        procedAssignment(distances, original, anonym, metaData);
    }
    
    /* Die Gewichte der ordinalen, nominalen und numerischen
     * Variablen werden standardisiert. 
     * 
     * @param md 
     */
     private void standardizeWeights(MetaData md) 
    {
        double sumweight = 0;
        
        for(MetaColumn m: md.getColumns())
        {
            if(m.getCtype() != ColumnType.Identifier 
                    && m.getCtype() != ColumnType.Blocking)
                sumweight += m.getWeight();
        }
        
        for(MetaColumn m: md.getColumns())
        {
            if(sumweight != 0)
                if(m.getCtype() != ColumnType.Identifier && m.getCtype() != ColumnType.Blocking)
                      m.setWeight(m.getWeight()/sumweight);
            else
                m.setWeight(m.getCtype().standardWeight());    
        }
    }

    /* Den ordinalen Variablen wird ein numerischer Wert zugeordnet und
     * können danach sortiert werden. Dies dient der späteren 
     * Berechnung der Distanzen.
     * 
     * @param table
     * @param md 
     */
    private void sortOrdinalVariables(ArrayTable<Integer, Integer, String> table, MetaData md) 
    {
        ArrayList<DistanceElement> ordlist = new ArrayList<DistanceElement>();
        DistanceElement ord = null;
       
        for(MetaColumn m: md.getColumns())
        {
            if(m.getCtype() == ColumnType.Ordinal)
            {
                for(int i=0; i< table.rowMap().size(); i++)
                {
                    ord = new DistanceElement(table.at(i,m.getColumnIndex()), i);
                    ordlist.add(ord);
                }
                
                Collections.sort(ordlist);
            
            
            ordlist.get(0).setIndex2(1);
            
            for(int i=1; i < ordlist.size(); i++)
            {
                if( ordlist.get(i).getWert().equals(ordlist.get(i-1).getWert()))
                    ordlist.get(i).setIndex2(ordlist.get(i-1).getIndex2());
                else
                    ordlist.get(i).setIndex2(ordlist.get(i-1).getIndex2()+1);
            }
            
            for(DistanceElement o: ordlist)
            {
                table.put(o.getIndex1(), m.getColumnIndex(), new Integer(o.getIndex2()).toString());
            }
            }
        }
        
    }

    /* Berechnet die Distanzen der einzelnen Zeilen zwischen den der originalen
     * und anonymisierten Datei
     * @param original
     * @param anonym
     * @param md
     * @return
     * @throws Exception 
     */
    private double[][] calculateDistances(ArrayTable<Integer, Integer, String> original,
                                    ArrayTable<Integer, Integer, String> anonym,
                                    MetaData md) throws Exception
    {
        double[][] distances = new double[anonym.rowMap().size()][original.rowMap().size()];
        double[][] distances_temp = new double[original.rowMap().size()][md.getColumns().size()];
        double[] distance_min = new double[md.getColumns().size()];
        double[] distance_max = new double[md.getColumns().size()];
        
        for(int i=0; i < anonym.rowMap().size(); i++)
        {
            for(int j=0; j < md.getColumns().size(); j++)
            {
                for(int k=0; k < original.rowMap().size(); k++)
                {
                   distances_temp[k][j] = Double.parseDouble(anonym.at(i, j));
                }
            }
            
            for(int l=0; l < md.getColumns().size(); l++)
            {
                if(md.getColumn(l).getCtype() == ColumnType.Blocking
                   || md.getColumn(l).getCtype() == ColumnType.Nominal)
                {
                    for(int z=0; z < original.rowMap().size(); z++)
                    {
                        if(distances_temp[z][l] - Double.parseDouble(original.at(z, l)) == 0)
                            distances_temp[z][l] = 0;
                        else
                            distances_temp[z][l] = 1;
                    }
                }
                else
                {
                    for(int z=0; z < original.rowMap().size(); z++)
                    {
                        distances_temp[z][l] = distances_temp[z][l] - Double.parseDouble(original.at(z, l));
                       
                        if(distances_temp[z][l] < 0)
                            distances_temp[z][l] *= -1;
                    }
                }
            }
            
            for(int j=0; j < md.getColumns().size(); j++)
            {
                distance_min[j] = distances_temp[0][j];
                distance_max[j] = distances_temp[0][j];
                
                for(int k=0; k < original.rowMap().size(); k++)
                {
                    distance_min[j] = Math.min(distance_min[j], distances_temp[k][j]);
                    distance_max[j] = Math.max(distance_max[j], distances_temp[k][j]);
                }
            }
            
            for(int j=0; j < md.getColumns().size(); j++)
            {
                for(int k=0; k < original.rowMap().size(); k++)
                {
                    if(distance_min[j] == distance_max[j])
                        distances_temp[k][j] = 0;
                    else
                        distances_temp[k][j] = (distances_temp[k][j] - distance_min[j]) / (distance_max[j] - distance_min[j]);
                }
            }
            
            for(int j=0; j < original.rowMap().size(); j++)
            {
                double summe = 0;
                
                for(int k=0; k < md.getColumns().size(); k++)
                {
                    summe += md.getColumn(k).getWeight()*Math.sqrt(distances_temp[j][k]);
                }
                distances[i][j] = Math.sqrt(summe);
            }
        }
        
        return distances;
    }

    private void procedAssignment(double[][] distances,
                                  ArrayTable<Integer, Integer, String> original,
                                  ArrayTable<Integer, Integer, String> anonym,
                                  MetaData md) 
    {
        int number_assignments = 0;
        int number_real_assignments = 0;
        int number_possible_assignements = 0;
        int identifier_index = 0;
        double treshmark = 0;
        int[] lab_zeil = new int[anonym.rowMap().size()];
        int[] lab_spalt = new int[original.rowMap().size()];
        boolean match = false;
        
        ArrayList<DistanceElement> mod = new ArrayList<DistanceElement>((original.rowMap().size())*(anonym.rowMap().size()));
        
        for(int i=0; i < (original.rowMap().size())*(anonym.rowMap().size()); i++ )
            mod.add(new DistanceElement());
        
        for(MetaColumn m: md.getColumns())
        {
            if(m.getCtype() == ColumnType.Identifier)
            {
                identifier_index = m.getColumnIndex();
                break;
            }
        }
        
        for(int i=0; i < anonym.rowMap().size(); i++)
        {
            for(int j=0; j < original.rowMap().size(); j++)
            {
                mod.get(i * (original.rowMap().size()) + j).setWert(new Double(distances[i][j]).toString());
                mod.get(i * (original.rowMap().size()) + j).setIndex1(i);
                mod.get(i * (original.rowMap().size()) + j).setIndex2(j);
            }
        }
        
        Collections.sort(mod);
        
        for(int i=0; i < lab_zeil.length;i++)
            lab_zeil[i] = 0;
        
        for(int i=0; i < lab_spalt.length;i++)
            lab_spalt[i] = 0;
        
        for(int i=0; number_assignments < Math.min(original.rowMap().size(), anonym.rowMap().size()); i++)
        {
            if(lab_zeil[mod.get(i).getIndex1()] == 0 && lab_spalt[mod.get(i).getIndex2()] == 0)
            {
                match = true;
                number_assignments++;
                lab_zeil[mod.get(i).getIndex1()] = 1;
                lab_spalt[mod.get(i).getIndex2()] = 1;
                
                   for(MetaColumn m: md.getColumns())
                   {
                       if(m.getCtype() != ColumnType.Identifier && m.getCtype() != ColumnType.Blocking && m.isKeyVariable())
                       {
                             if(original.at(mod.get(i).getIndex2(), m.getColumnIndex()).equals("0"))
                                treshmark = 0;
                             else
                             {
                                 treshmark = (new Double(original.at(mod.get(i).getIndex2(), m.getColumnIndex())).doubleValue() -
                                                new Double(anonym.at(mod.get(i).getIndex1(), m.getColumnIndex())).doubleValue()) /
                                                    new Double(original.at(mod.get(i).getIndex2(), m.getColumnIndex())).doubleValue();
                                 
                                 if(treshmark < 0)
                                     treshmark *= -1;
                                 
                                 if(treshmark > m.getTreshhold())
                                 {
                                     match = false;
                                     break;
                                 }
                             }
                       }
                   }
                   if (match)
                   {
                        number_possible_assignements++; 
                   
                    if(anonym.at(mod.get(i).getIndex1(), identifier_index).
                        equals(original.at(mod.get(i).getIndex2(), identifier_index)))
                    {
                         number_real_assignments++;
                    }
                  }
            }
        }
        real_matches = (new Double(number_real_assignments).doubleValue() / new Double(number_assignments).doubleValue()) * 100;
        possible_matches = (new Double(number_possible_assignements).doubleValue() / new Double(number_assignments).doubleValue()) * 100;
    }
    
    public MatchingResult getMatchingResults() {
        return new MatchingResult(possible_matches, real_matches);
    }
  
}