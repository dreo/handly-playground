/*******************************************************************************
 * Copyright (c) 2014 1C LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Vladimir Piskarev (1C) - initial API and implementation
 *******************************************************************************/
package generator;

import java.util.Random;

public class Generator
{
    public static void main(String[] args)
    {
        Random r = new Random();
        for (int i = 1; i <= 15000; i++)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("def f");
            sb.append(i);
            sb.append("(");
            int n = r.nextInt(4);
            switch (n)
            {
            case 1:
                sb.append("x");
                break;
            case 2:
                sb.append("x,y");
                break;
            case 3:
                sb.append("x,y,z");
                break;
            }
            sb.append(") {\n}\n");
            System.out.println(sb);
        }
    }
}
