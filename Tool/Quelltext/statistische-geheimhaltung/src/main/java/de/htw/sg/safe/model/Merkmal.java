package de.htw.sg.safe.model;


/**
 * Spalte einer {@link StatistikDatei}
 * 
 * @author Boris
 */
public class Merkmal
{
    private String name;
    private MerkmalTyp typ;
    
    public Merkmal(String name, MerkmalTyp typ)
    {
        super();
        this.name = name;
        this.typ = typ;
    }
    public String getName()
    {
        return name;
    }
    public MerkmalTyp getTyp()
    {
        return typ;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((typ == null) ? 0 : typ.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof Merkmal))
        {
            return false;
        }
        Merkmal other = (Merkmal) obj;
        if (name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        }
        else if (!name.equals(other.name))
        {
            return false;
        }
        if (typ != other.typ)
        {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString()
    {
        return "Merkmal [name=" + name + ", typ=" + typ + "]";
    }
}
