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

package net.sf.ezmorph.primitive;

import net.sf.ezmorph.MorphException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Morphs to a long.
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public final class LongMorpher extends AbstractIntegerMorpher
{
   private long defaultValue;

   public LongMorpher()
   {
      super();
   }

   public LongMorpher( long defaultValue )
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

      if( !(obj instanceof LongMorpher) ){
         return false;
      }

      LongMorpher other = (LongMorpher) obj;
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

   public long getDefaultValue()
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

   /**
    * Morphs the input object into an output object of the supported type.
    *
    * @param value The input value to be morphed
    * @exception MorphException if conversion cannot be performed successfully
    */
   public long morph( Object value )
   {
      if( value == null ){
         if( isUseDefault() ){
            return defaultValue;
         }else{
            throw new MorphException( "value is null" );
         }
      }

      if( value instanceof Number ){
         return ((Number) value).longValue();
      }else{
         long i = 0;
         try{
            i = Long.parseLong( getIntegerValue( value ) );
            return i;
         }
         catch( NumberFormatException nfe ){
            if( isUseDefault() ){
               return defaultValue;
            }else{
               throw new MorphException( nfe );
            }
         }
      }
   }

   public Class morphsTo()
   {
      return Long.TYPE;
   }
}