package de.htw.pim.sg.methods.multiplicative_stochastic_noise;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import java.util.*;

/**
 * User: psy-xoryves (Viktor Dillmann)
 * Date: 11.01.12
 * Time: 23:49
 */
//TODO implements AnonymizingInterface
public class MultiplicativeStochasticNoise
{
  private ArrayTable<Integer, Integer, Double> csv;
  private int csvRowCount = 0;
  private int csvColumnCount = 0;
  private Map<Integer, Double> averageDimensions;
  private Map<Integer, Double> scores;
  private ArrayTable<Integer, Integer, Double> csvAnonymized;
  
  public MultiplicativeStochasticNoise(Table<Integer, Integer, Double> table)
  {
    csv = (ArrayTable) table;
    csvAnonymized = ArrayTable.create(csv);
    init();
  }

  /**
   * Initialises column/row count, avarage dimensions and scores.
   */
  private void init()
  {
    csvColumnCount    = csv.row(0).size();
    csvRowCount       = csv.column(0).size();
    averageDimensions = calculateAverageDimensions();
    scores            = calculateScores();
  }

  /**
   * Calculates from every column the arithmetic mean/average.
   *
   * @return Map with Integer=column and String=arithmetic mean/average
   */
  private Map<Integer, Double> calculateAverageDimensions()
  {
    Map<Integer, Double> dimensions = new HashMap<Integer, Double>();

    //each column
    Map<Integer, Map<Integer, Double>> columns = csv.columnMap();
    for(Map.Entry<Integer, Map<Integer, Double>> columnEntry : columns.entrySet())
    {
      double value              = 0.0;
      Map<Integer, Double> rows = columnEntry.getValue();

      //each row
      for(Map.Entry<Integer, Double> rowEntry : rows.entrySet())
      {
        double rowValue = rowEntry.getValue();
        if(!Double.isNaN(rowValue))
        {
          value += rowEntry.getValue();
        }
      }

      value /= csvRowCount;
      dimensions.put(columnEntry.getKey(), value);
    }

    return dimensions;
  }

  /**
   * Calculate for each row the Score.
   *
   * @return Map with Integer=row and String=Score
   */
  private Map<Integer, Double> calculateScores()
  {
    Map<Integer, Double> scores = new HashMap<Integer, Double>();

    //each row
    Map<Integer, Map<Integer, Double>> rows = csv.rowMap();
    for(Map.Entry<Integer, Map<Integer, Double>> rowEntry : rows.entrySet())
    {
      double value = 0.0;
      int missing  = 0;
      Map<Integer, Double> columns = rowEntry.getValue();

      //each column
      for(Map.Entry<Integer, Double> columnEntry : columns.entrySet())
      {
        double columnValue = columnEntry.getValue();
        if(!Double.isNaN(columnValue))
        {
          double xij = Math.abs(columnValue);
          xij /= averageDimensions.get(columnEntry.getKey());

          value += xij;
        }
        else
        {
          missing += 1;
        }
      }

      value /= (csvColumnCount - missing);
      scores.put(rowEntry.getKey(), value);
    }

    return scores;
  }

  /**
   * Controlled multiplicative stochastic noise for given column.
   *
   * @param column
   */
  public Table<Integer, Integer, Double> run(int column, double f, double s)
  {
    DoubleValueComparator dvc = new DoubleValueComparator(scores);
    TreeMap<Integer, Double> sortedScores = new TreeMap(dvc);
    sortedScores.putAll(scores);

    Random random = new Random();

    Iterator<Integer> iter = sortedScores.keySet().iterator();
    //anonymize first value
    if(iter.hasNext())
    {
      int row = iter.next();
      // X0
      double value = csv.get(row, column);
      // Xa = (1 + fz* + z#) * X0
      //double valueAnonymized = anonymize(value, -1, random);
      double valueAnonymized = anonymize(value, -1, f, s);

      csvAnonymized.put(row, column, valueAnonymized);
    }

    // controlled multiplicative stochastic noise
    for(int row = -1; iter.hasNext(); )
    {
      row = iter.next();
      // x0
      double value = csv.get(row, column);
      // Xa = (1 + fz* + z#) * X0, z*=-1
      //double valueAnonymizedNegative = anonymize(value, -1, random);
      double valueAnonymizedNegative = anonymize(value, -1, f, s);
      // Xa = (1 + fz* + z#) * X0, z*=+1
      //double valueAnonymizedPositive = anonymize(value, 1, random);
      double valueAnonymizedPositive = anonymize(value, 1, f, s);

      // calculate for zi*=-1 and zi*=+1 the normed divergence
      // of the sum of original and anonymized data
      csvAnonymized.put(row, column, valueAnonymizedNegative);
      double aiNeg = divergence(sortedScores, row);
      csvAnonymized.put(row, column, valueAnonymizedPositive);
      double aiPos = divergence(sortedScores, row);

      // if zi*=-1 generates the smaller divergence
      if(aiNeg < aiPos)
      {
        csvAnonymized.put(row, column, valueAnonymizedNegative);
      }
    }
    
    return csvAnonymized;
  }

  /**
   * Anonymize a value...
   *
   * @param value = data to anonymize
   * @param z = levelshift +-1 (z*)
   * random = Random Class
   * @return anonymized data as double
   */
  //private double anonymize(double value, int z, Random random)
  private double anonymize(double value, int z, double f, double s)
  {
    double valueAnonymized = 0.0;

    // f
    //double f = random.nextGaussian();

    // s with f>s == z# == ~N(0,s)
    //TODO sinnvoll das so zu machen?
    //double s = f;
    //while(f <= s )
    //{
      //s = random.nextGaussian();
    //}

    valueAnonymized = (1 + f*z + s) * value;
    
    return valueAnonymized;
  }

  /**
   * Calculate the normed divergence between anonymized and original data.
   * Ai ...
   *
   * @param sortedScores
   * @param row
   * @return
   */
  private double divergence(TreeMap<Integer, Double> sortedScores, int row)
  {
    //Ai
    double sumColumns = 0.0;

    //each column
    Map<Integer, Map<Integer, Double>> columns = csv.columnMap();
    for(Map.Entry<Integer, Map<Integer, Double>> c : columns.entrySet())
    {
      double sumRows = 0.0;

      //each row until actual to anonymize row
      Iterator<Integer> sortedRows = sortedScores.keySet().iterator();
      for(int i=-1; i!=row; )
      {
        i = sortedRows.next();
        double xa = csvAnonymized.get(i, c.getKey());
        double xo = csv.get(i, c.getKey());
        sumRows += Math.abs(xa-xo);
      }
      sumColumns += sumRows/averageDimensions.get(c.getKey());
    }

    return sumColumns;
  }
}

/**
 * For sorting the Score in descending order
 */
class DoubleValueComparator implements Comparator
{
  Map<Integer, Double> base;
  public DoubleValueComparator(Map<Integer, Double> base)
  {
    this.base = base;
  }

  public int compare(Object a, Object b)
  {
    if(base.get(a) < base.get(b))
    {
      return 1;
    }
    else if(Double.doubleToLongBits(base.get(a)) == Double.doubleToLongBits(base.get(b)))
    {
      return 0;
    }
    else
    {
      return -1;
    }
  }
}