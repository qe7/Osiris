package lunatrius.utils;

public class Vector3f
{
    public float x;
    public float y;
    public float z;
    public static final Vector3f ZERO = new Vector3f();

    public Vector3f()
    {
        x = 0.0F;
        y = 0.0F;
        z = 0.0F;
    }

    public Vector3f(float f, float f1, float f2)
    {
        x = f;
        y = f1;
        z = f2;
    }

    public Vector3f add(Vector3f vector3f)
    {
        return add(vector3f.x, vector3f.y, vector3f.z);
    }

    public Vector3f add(float f)
    {
        return add(f, f, f);
    }

    public Vector3f add(float f, float f1, float f2)
    {
        x += f;
        y += f1;
        z += f2;
        return this;
    }

    public Vector3f sub(Vector3i vector3i)
    {
        return sub(vector3i.x, vector3i.y, vector3i.z);
    }

    public Vector3f sub(float f)
    {
        return sub(f, f, f);
    }

    public Vector3f sub(float f, float f1, float f2)
    {
        x -= f;
        y -= f1;
        z -= f2;
        return this;
    }

    public Vector3f clone()
    {
        return new Vector3f(x, y, z);
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof Vector3f))
        {
            return false;
        }
        else
        {
            Vector3f vector3f = (Vector3f)obj;
            return x == vector3f.x && y == vector3f.y && z == vector3f.z;
        }
    }

    public int hashCode()
    {
        int i = 7;
        i = (int)((float)(71 * i) + x);
        i = (int)((float)(71 * i) + y);
        i = (int)((float)(71 * i) + z);
        return i;
    }
}
