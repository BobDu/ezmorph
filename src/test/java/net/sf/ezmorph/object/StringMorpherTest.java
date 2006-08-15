/*
 * Copyright 2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sf.ezmorph.object;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.ezmorph.Morpher;
import net.sf.ezmorph.test.ArrayAssertions;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class StringMorpherTest extends AbstractObjectMorpherTestCase
{
   public static void main( String[] args )
   {
      TestRunner.run( suite() );
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite( StringMorpherTest.class );
      suite.setName( "StringMorpher Tests" );
      return suite;
   }

   private StringMorpher anotherMorpher;
   private StringMorpher anotherMorpherWithDefaultValue;
   private StringMorpher morpher;
   private StringMorpher morpherWithDefaultValue;

   public StringMorpherTest( String name )
   {
      super( name );
   }

   // -----------------------------------------------------------------------

   public void testMorph_boolean()
   {
      String expected = "true";
      String actual = (String) morpher.morph( Boolean.TRUE );
      assertEquals( expected, actual );
   }

   public void testMorph_noConversion()
   {
      String expected = "true";
      String actual = (String) morpher.morph( expected );
      assertEquals( expected, actual );
   }

   public void testMorph_null()
   {
      assertNull( morpher.morph( null ) );
   }

   public void testMorph_useDefault()
   {
      String expected = "";
      morpher = new StringMorpher( "" );
      String actual = (String) morpher.morph( null );
      assertEquals( expected, actual );
   }

   public void testMorph_useDefault2()
   {
      String expected = "";
      morpher = new StringMorpher( expected );
      String actual = (String) morpher.morph( null );
      assertEquals( expected, actual );
   }

   public void testMorph_array()
   {
      ArrayAssertions.assertEquals( "[true, false]", morpher.morph( new boolean[] { true, false } ) );
      ArrayAssertions.assertEquals( "[A, B]", morpher.morph( new char[] { 'A', 'B' } ) );
      ArrayAssertions.assertEquals( "[one, two]", morpher.morph( new String[] { "one", "two" } ) );
   }

   public void testMorph_array_numbers()
   {
      String expected = "[1, 2]";
      ArrayAssertions.assertEquals( expected, morpher.morph( new byte[] { 1, 2 } ) );
      ArrayAssertions.assertEquals( expected, morpher.morph( new short[] { 1, 2 } ) );
      ArrayAssertions.assertEquals( expected, morpher.morph( new int[] { 1, 2 } ) );
      ArrayAssertions.assertEquals( expected, morpher.morph( new long[] { 1, 2 } ) );

      expected = "[1.0, 2.0]";
      ArrayAssertions.assertEquals( expected, morpher.morph( new float[] { 1, 2 } ) );
      ArrayAssertions.assertEquals( expected, morpher.morph( new double[] { 1, 2 } ) );
   }

   protected Morpher getAnotherMorpher()
   {
      return anotherMorpher;
   }

   protected Morpher getAnotherMorpherWithDefaultValue()
   {
      return anotherMorpherWithDefaultValue;
   }

   protected Morpher getMorpher()
   {
      return morpher;
   }

   protected Morpher getMorpherWithDefaultValue()
   {
      return morpherWithDefaultValue;
   }

   protected void setUp() throws Exception
   {
      morpher = new StringMorpher();
      morpherWithDefaultValue = new StringMorpher( "" );
      anotherMorpher = new StringMorpher();
      anotherMorpherWithDefaultValue = new StringMorpher( "..." );
   }
}