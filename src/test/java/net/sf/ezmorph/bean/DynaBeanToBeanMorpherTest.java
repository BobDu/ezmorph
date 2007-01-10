/*
 * Copyright 2006-2007 the original author or authors.
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

package net.sf.ezmorph.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.MorphUtils;
import net.sf.ezmorph.MorpherRegistry;
import net.sf.ezmorph.bean.sample.BeanA;
import net.sf.ezmorph.bean.sample.BeanB;
import net.sf.ezmorph.bean.sample.BeanC;
import net.sf.ezmorph.test.ArrayAssertions;

import org.apache.commons.beanutils.DynaBean;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class DynaBeanToBeanMorpherTest extends TestCase
{
   public static void main( String[] args )
   {
      TestRunner.run( suite() );
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite( DynaBeanToBeanMorpherTest.class );
      suite.setName( "DynaBeanToBeanMorpher Tests" );
      return suite;
   }

   private MorpherRegistry morpherRegistry;

   public DynaBeanToBeanMorpherTest( String name )
   {
      super( name );
   }

   public void testException_array_class()
   {
      try{
         new DynaBeanToBeanMorpher( Map[].class, morpherRegistry );
         fail( "Expected an MorphException" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testException_Collection_subclass()
   {
      try{
         new DynaBeanToBeanMorpher( ArrayList.class, morpherRegistry );
         fail( "Expected an MorphException" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testException_DynaBean_class()
   {
      try{
         new DynaBeanToBeanMorpher( MorphDynaBean.class, morpherRegistry );
         fail( "Expected an MorphException" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testException_interface_class()
   {
      try{
         new DynaBeanToBeanMorpher( Map.class, morpherRegistry );
         fail( "Expected an MorphException" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testException_Map_subclass()
   {
      try{
         new DynaBeanToBeanMorpher( HashMap.class, morpherRegistry );
         fail( "Expected an MorphException" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testException_null_class()
   {
      try{
         new DynaBeanToBeanMorpher( null, morpherRegistry );
         fail( "Expected an MorphException" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testException_null_morpherRegistry()
   {
      try{
         new DynaBeanToBeanMorpher( BeanA.class, null );
         fail( "Expected an MorphException" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testException_primitive_class()
   {
      try{
         new DynaBeanToBeanMorpher( int.class, morpherRegistry );
         fail( "Expected an MorphException" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testException_String_class()
   {
      try{
         new DynaBeanToBeanMorpher( String.class, morpherRegistry );
         fail( "Expected an MorphException" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testException_unssuported_value()
   {
      try{
         DynaBeanToBeanMorpher morpher = new DynaBeanToBeanMorpher( BeanA.class, morpherRegistry );
         morpher.morph( new BeanB() );
         fail( "Expected a MorphException" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testException_wrapper_class()
   {
      try{
         new DynaBeanToBeanMorpher( Integer.class, morpherRegistry );
         fail( "Expected an MorphException" );
      }
      catch( MorphException expected ){
         // ok
      }
      try{
         new DynaBeanToBeanMorpher( Boolean.class, morpherRegistry );
         fail( "Expected an MorphException" );
      }
      catch( MorphException expected ){
         // ok
      }
      try{
         new DynaBeanToBeanMorpher( Character.class, morpherRegistry );
         fail( "Expected an MorphException" );
      }
      catch( MorphException expected ){
         // ok
      }
   }

   public void testMorph() throws Exception
   {
      Map properties = new HashMap();
      properties.put( "string", String.class );
      properties.put( "integer", Integer.class );
      properties.put( "bool", Boolean.class );
      MorphDynaClass dynaClass = new MorphDynaClass( "JSON", MorphDynaBean.class, properties );
      MorphDynaBean dynaBean = (MorphDynaBean) dynaClass.newInstance();
      dynaBean.setDynaBeanClass( dynaClass );
      dynaBean.set( "string", "dyna morph" );
      dynaBean.set( "integer", "24" );
      dynaBean.set( "bool", "false" );

      DynaBeanToBeanMorpher morpher = new DynaBeanToBeanMorpher( BeanA.class, morpherRegistry );
      BeanA beanA = (BeanA) morpher.morph( dynaBean );
      assertNotNull( beanA );
      assertEquals( false, beanA.isBool() );
      assertEquals( 24, beanA.getInteger() );
      assertEquals( "dyna morph", beanA.getString() );
   }

   public void testMorph_nested__dynaBeans() throws Exception
   {
      Map properties = new HashMap();
      properties.put( "string", String.class );
      properties.put( "integer", Integer.class );
      properties.put( "bool", Boolean.class );
      MorphDynaClass dynaClass = new MorphDynaClass( "JSON", MorphDynaBean.class, properties );
      MorphDynaBean dynaBeanA = (MorphDynaBean) dynaClass.newInstance();
      dynaBeanA.setDynaBeanClass( dynaClass );
      dynaBeanA.set( "string", "dyna morph" );
      dynaBeanA.set( "integer", "24" );
      dynaBeanA.set( "bool", "false" );

      properties = new HashMap();
      properties.put( "string", String.class );
      properties.put( "integer", Integer.class );
      properties.put( "bool", Boolean.class );
      properties.put( "intarray", int[].class );
      dynaClass = new MorphDynaClass( "JSON", MorphDynaBean.class, properties );
      MorphDynaBean dynaBeanB = (MorphDynaBean) dynaClass.newInstance();
      dynaBeanB.setDynaBeanClass( dynaClass );
      dynaBeanB.set( "string", "dyna morph B" );
      dynaBeanB.set( "integer", "48" );
      dynaBeanB.set( "bool", "true" );
      dynaBeanB.set( "intarray", new int[] { 4, 5, 6 } );

      properties = new HashMap();
      properties.put( "beanA", DynaBean.class );
      properties.put( "beanB", DynaBean.class );
      dynaClass = new MorphDynaClass( "JSON", MorphDynaBean.class, properties );
      MorphDynaBean dynaBeanC = (MorphDynaBean) dynaClass.newInstance();
      dynaBeanC.setDynaBeanClass( dynaClass );
      dynaBeanC.set( "beanA", dynaBeanA );
      dynaBeanC.set( "beanB", dynaBeanB );

      morpherRegistry.registerMorpher( new DynaBeanToBeanMorpher( BeanA.class, morpherRegistry ) );
      morpherRegistry.registerMorpher( new DynaBeanToBeanMorpher( BeanB.class, morpherRegistry ) );
      DynaBeanToBeanMorpher morpher = new DynaBeanToBeanMorpher( BeanC.class, morpherRegistry );
      BeanC beanC = (BeanC) morpher.morph( dynaBeanC );
      assertNotNull( beanC );
      BeanA beanA = beanC.getBeanA();
      assertEquals( false, beanA.isBool() );
      assertEquals( 24, beanA.getInteger() );
      assertEquals( "dyna morph", beanA.getString() );
      BeanB beanB = beanC.getBeanB();
      assertEquals( true, beanB.isBool() );
      assertEquals( 48, beanB.getInteger() );
      assertEquals( "dyna morph B", beanB.getString() );
      ArrayAssertions.assertEquals( new int[] { 4, 5, 6 }, beanB.getIntarray() );
   }

   public void testMorph_nested__specific_classes() throws Exception
   {
      Map properties = new HashMap();
      properties.put( "beanA", BeanA.class );
      properties.put( "beanB", BeanB.class );
      MorphDynaClass dynaClass = new MorphDynaClass( "JSON", MorphDynaBean.class, properties );
      MorphDynaBean dynaBean = (MorphDynaBean) dynaClass.newInstance();
      dynaBean.setDynaBeanClass( dynaClass );
      dynaBean.set( "beanA", new BeanA() );
      dynaBean.set( "beanB", new BeanB() );

      DynaBeanToBeanMorpher morpher = new DynaBeanToBeanMorpher( BeanC.class, morpherRegistry );
      BeanC beanC = (BeanC) morpher.morph( dynaBean );
      assertNotNull( beanC );
      BeanA beanA = beanC.getBeanA();
      assertEquals( true, beanA.isBool() );
      assertEquals( 42, beanA.getInteger() );
      assertEquals( "morph", beanA.getString() );
      BeanB beanB = beanC.getBeanB();
      assertEquals( true, beanB.isBool() );
      assertEquals( 42, beanB.getInteger() );
      assertEquals( "morph", beanB.getString() );
      ArrayAssertions.assertEquals( new int[] { 1, 2, 3 }, beanB.getIntarray() );
   }

   public void testMorph_null()
   {
      DynaBeanToBeanMorpher morpher = new DynaBeanToBeanMorpher( BeanA.class, morpherRegistry );
      BeanA beanA = (BeanA) morpher.morph( null );
      assertNull( beanA );
   }

   protected void setUp() throws Exception
   {
      morpherRegistry = new MorpherRegistry();
      MorphUtils.registerStandardMorphers( morpherRegistry );
   }
}