package lunatrius.utils;

public class Vector3i
{
    public int x;
    public int y;
    public int z;
    public static final Vector3i ZERO = new Vector3i();

    public Vector3i()
    {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3i(int i, int j, int k)
    {
        x = i;
        y = j;
        z = k;
    }

    public Vector3i add(Vector3i vector3i)
    {
        return add(vector3i.x, vector3i.y, vector3i.z);
    }

    public Vector3i add(int i)
    {
        return add(i, i, i);
    }

    public Vector3i add(int i, int j, int k)
    {
        x += i;
        y += j;
        z += k;
        return this;
    }

    public Vector3i sub(Vector3i vector3i)
    {
        return sub(vector3i.x, vector3i.y, vector3i.z);
    }

    public Vector3i sub(int i)
    {
        return sub(i, i, i);
    }

    public Vector3i sub(int i, int j, int k)
    {
        x -= i;
        y -= j;
        z -= k;
        return this;
    }

    public Vector3i clone()
    {
        return new Vector3i(x, y, z);
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof Vector3i))
        {
            return false;
        }
        else
        {
            Vector3i vector3i = (Vector3i)obj;
            return x == vector3i.x && y == vector3i.y && z == vector3i.z;
        }
    }

    public int hashCode()
    {
        int i = 7;
        i = 71 * i + x;
        i = 71 * i + y;
        i = 71 * i + z;
        return i;
    }
}
