package net.sf.ezmorph.object;

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.Morpher;

public class BigDecimalMorpherTest extends AbstractObjectMorpherTestCase
{

   public static void main( String[] args )
   {
      TestRunner.run( suite() );
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite( BigDecimalMorpherTest.class );
      suite.setName( "BigDecimalMorpher Tests" );
      return suite;
   }

   private BigDecimalMorpher anotherMorpher;
   private BigDecimalMorpher anotherMorpherWithDefaultValue;
   private BigDecimalMorpher morpher;
   private BigDecimalMorpher morpherWithDefaultValue;

   public BigDecimalMorpherTest( String name )
   {
      super( name );
   }

   // -----------------------------------------------------------------------

   public void testBigDecimalMorph_BigDecimal()
   {
      Object actual = ((BigDecimalMorpher) getMorpherWithDefaultValue()).morph( BigDecimal.ZERO );
      assertEquals( BigDecimal.ZERO, actual );
   }
   
   public void testBigDecimalMorph_BigInteger()
   {
      Object actual = ((BigDecimalMorpher) getMorpherWithDefaultValue()).morph( BigInteger.ZERO );
      assertEquals( BigDecimal.ZERO, actual );
   }

   public void testBigDecimalMorph_Number()
   {
      Object actual = ((BigDecimalMorpher) getMorpherWithDefaultValue()).morph( new Double( 1 ) );
      assertEquals( new BigDecimal( 1d ), actual );
   }

   public void testBigDecimalMorph_Number__Double_INFINITY()
   {
      try{
         ((BigDecimalMorpher) getMorpher()).morph( new Double( Double.POSITIVE_INFINITY ) );
         fail( "Should have thrown an Exception" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testBigDecimalMorph_Number__Double_NAN()
   {
      try{
         ((BigDecimalMorpher) getMorpher()).morph( new Double( Double.NaN ) );
         fail( "Should have thrown an Exception" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testBigDecimalMorph_Number__Float_INFINITY()
   {
      try{
         ((BigDecimalMorpher) getMorpher()).morph( new Float( Float.POSITIVE_INFINITY ) );
         fail( "Should have thrown an Exception" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testBigDecimalMorph_Number__Float_NAN()
   {
      try{
         ((BigDecimalMorpher) getMorpher()).morph( new Float( Float.NaN ) );
         fail( "Should have thrown an Exception" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testBigDecimalMorph_String()
   {
      Object actual = ((BigDecimalMorpher) getMorpherWithDefaultValue()).morph( "123.45" );
      assertEquals( new BigDecimal( "123.45" ), actual );
   }
   
   public void testBigDecimalMorph_String_empty()
   {
      assertNull( ((BigDecimalMorpher)getMorpher()).morph( "" ) );
   }
   
   public void testBigDecimalMorph_String_null()
   {
      assertNull( ((BigDecimalMorpher)getMorpher()).morph( null ) );
   }
   
   public void testBigDecimalMorph_String_null2()
   {
      assertNull( ((BigDecimalMorpher)getMorpher()).morph( "null" ) );
   }

   public void testBigDecimalMorph_throwException()
   {
      try{
         ((BigDecimalMorpher) getMorpher()).morph( String.valueOf( "A" ) );
         fail( "Should have thrown an Exception" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testBigDecimalMorph_useDefault()
   {
      String expected = String.valueOf( "A" );
      Object actual = ((BigDecimalMorpher) getMorpherWithDefaultValue()).morph( expected );
      assertEquals( BigDecimal.ZERO, actual );
   }

   public void testBigDecimalMorph_useDefault_null()
   {
      Object actual = ((BigDecimalMorpher) getMorpherWithDefaultValue()).morph( null );
      assertEquals( BigDecimal.ZERO, actual );
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
      morpher = new BigDecimalMorpher();
      morpherWithDefaultValue = new BigDecimalMorpher( BigDecimal.ZERO );
      anotherMorpher = new BigDecimalMorpher();
      anotherMorpherWithDefaultValue = new BigDecimalMorpher( BigDecimal.ONE );
   }
}