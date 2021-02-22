/*
* include package file and import files *
*
* */
package com.utiltity;

import java.io.IOException;

/**
* File : AppendableObjectInputStream File
* Description :Receive data of object type 
* 
* @author Alan, Anish, Ninad ,Rohan
*
*/	


/**
*
* Class AppendableObjectInputStream : AppendableObjectInputStream
*
*/
import java.io.InputStream;
import java.io.ObjectInputStream;

public class AppendableObjectInputStream extends ObjectInputStream
{

    public AppendableObjectInputStream(InputStream in) throws IOException
    {
        super(in);
    }

    @Override
    protected void readStreamHeader() throws IOException
    {
        // do not read a header
    }


}
/************************************************************** endoffile************************************************************************/