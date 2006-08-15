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

import java.util.Arrays;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Morphs to a String.
 * 
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class StringMorpher extends AbstractObjectMorpher
{
   private String defaultValue;

   public StringMorpher()
   {
      super( false );
   }

   public StringMorpher( String defaultValue )
   {
      super( true );
      this.defaultValue = defaultValue;
   }

   public boolean equals( Object obj )
   {
      if( this == obj ){
         return true;
      }
      if( obj == null ){
         return false;
      }

      if( !(obj instanceof StringMorpher) ){
         return false;
      }

      StringMorpher other = (StringMorpher) obj;
      EqualsBuilder builder = new EqualsBuilder();
      if( isUseDefault() && other.isUseDefault() ){
         builder.append( getDefaultValue(), other.getDefaultValue() );
         return builder.isEquals();
      }else if( !isUseDefault() && !other.isUseDefault() ){
         return builder.isEquals();
      }else{
         return false;
      }
   }

   public String getDefaultValue()
   {
      return defaultValue;
   }

   public int hashCode()
   {
      HashCodeBuilder builder = new HashCodeBuilder();
      if( isUseDefault() ){
         builder.append( getDefaultValue() );
      }
      return builder.toHashCode();
   }

   public Object morph( Object value )
   {
      if( isUseDefault() ){
         return getDefaultValue();
      }

      if( value == null ){
         return null;
      }

      if( String.class.isAssignableFrom( value.getClass() ) ){
         return (String) value;
      }

      Class sourceClass = value.getClass();
      if( sourceClass.isArray() ){
         if( boolean[].class.isAssignableFrom( sourceClass ) ){
            return Arrays.toString( (boolean[]) value );
         }else if( char[].class.isAssignableFrom( sourceClass ) ){
            return Arrays.toString( (char[]) value );
         }else if( byte[].class.isAssignableFrom( sourceClass ) ){
            return Arrays.toString( (byte[]) value );
         }else if( short[].class.isAssignableFrom( sourceClass ) ){
            return Arrays.toString( (short[]) value );
         }else if( int[].class.isAssignableFrom( sourceClass ) ){
            return Arrays.toString( (int[]) value );
         }else if( long[].class.isAssignableFrom( sourceClass ) ){
            return Arrays.toString( (long[]) value );
         }else if( float[].class.isAssignableFrom( sourceClass ) ){
            return Arrays.toString( (float[]) value );
         }else if( double[].class.isAssignableFrom( sourceClass ) ){
            return Arrays.toString( (double[]) value );
         }else{
            return Arrays.toString( (Object[]) value );
         }
      }

      return String.valueOf( value );
   }

   public Class morphsTo()
   {
      return String.class;
   }

   public boolean supports( Class clazz )
   {
      return true;
   }
}