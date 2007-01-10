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

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Map;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.MorpherRegistry;
import net.sf.ezmorph.ObjectMorpher;
import net.sf.ezmorph.object.IdentityObjectMorpher;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Converts a DynaBean into an specific bean.<br>
 * This Morpher will try to match every property from the target bean's class to
 * the properties of the source DynaBean. If a bean property and the dyna
 * property differ in type, it will try to morph it. If a Morpher is not found
 * for that type, the conversion will be aborted with a MorphException; this may
 * be changed by setting the Morpher to be lenient, in that way it will ignore
 * the property (the resulting value will be null).
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public final class DynaBeanToBeanMorpher implements ObjectMorpher
{
   private static final Log log = LogFactory.getLog( DynaBeanToBeanMorpher.class );
   private final Class beanClass;
   private boolean lenient;
   private final MorpherRegistry morpherRegistry;

   public DynaBeanToBeanMorpher( Class beanClass, MorpherRegistry morpherRegistry )
   {
      this( beanClass, morpherRegistry, false );
   }

   public DynaBeanToBeanMorpher( Class beanClass, MorpherRegistry morpherRegistry, boolean lenient )
   {
      validateClass( beanClass );
      if( morpherRegistry == null ){
         throw new MorphException( "morpherRegistry is null" );
      }
      this.beanClass = beanClass;
      this.morpherRegistry = morpherRegistry;
      this.lenient = lenient;
   }

   public Object morph( Object value )
   {
      if( value == null ){
         return null;
      }
      if( !supports( value.getClass() ) ){
         throw new MorphException( "value is not a DynaBean" );
      }

      Object bean = null;

      try{
         bean = beanClass.newInstance();

         DynaBean dynaBean = (DynaBean) value;
         DynaClass dynaClass = dynaBean.getDynaClass();
         PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors( beanClass );
         for( int i = 0; i < pds.length; i++ ){
            PropertyDescriptor pd = pds[i];
            String name = pd.getName();
            DynaProperty dynaProperty = dynaClass.getDynaProperty( name );
            if( dynaProperty != null ){
               Class dynaType = dynaProperty.getType();
               Class type = pd.getPropertyType();
               if( type.isAssignableFrom( dynaType ) ){
                  PropertyUtils.setProperty( bean, name, dynaBean.get( name ) );
               }else{
                  if( IdentityObjectMorpher.getInstance() == morpherRegistry.getMorpherFor( type ) ){
                     if( !lenient ){
                        throw new MorphException( "Can't find a morpher for target class " + type
                              + " (" + name + ")" );
                     }else{
                        log.info( "Can't find a morpher for target class " + type + " (" + name
                              + ") SKIPPED" );
                     }
                  }else{
                     PropertyUtils.setProperty( bean, name, morpherRegistry.morph( type,
                           dynaBean.get( name ) ) );
                  }
               }
            }
         }
      }
      catch( MorphException me ){
         throw me;
      }
      catch( Exception e ){
         throw new MorphException( e );
      }

      return bean;
   }

   public Class morphsTo()
   {
      return beanClass;
   }

   public boolean supports( Class clazz )
   {
      return DynaBean.class.isAssignableFrom( clazz );
   }

   private void validateClass( Class clazz )
   {
      if( clazz == null ){
         throw new MorphException( "target class is null" );
      }else if( clazz.isPrimitive() ){
         throw new MorphException( "target class is a primitive" );
      }else if( clazz.isArray() ){
         throw new MorphException( "target class is an array" );
      }else if( clazz.isInterface() ){
         throw new MorphException( "target class is an interface" );
      }else if( DynaBean.class.isAssignableFrom( clazz ) ){
         throw new MorphException( "target class is a DynaBean" );
      }else if( Number.class.isAssignableFrom( clazz ) || Boolean.class.isAssignableFrom( clazz )
            || Character.class.isAssignableFrom( clazz ) ){
         throw new MorphException( "target class is a wrapper" );
      }else if( String.class.isAssignableFrom( clazz ) ){
         throw new MorphException( "target class is a String" );
      }else if( Collection.class.isAssignableFrom( clazz ) ){
         throw new MorphException( "target class is a Collection" );
      }else if( Map.class.isAssignableFrom( clazz ) ){
         throw new MorphException( "target class is a Map" );
      }
   }
}