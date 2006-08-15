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

package net.sf.ezmorph.array;

import java.lang.reflect.Array;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.primitive.CharMorpher;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Morphs an array to a char[].
 * 
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class CharArrayMorpher extends AbstractArrayMorpher
{
   private static final Class CHAR_ARRAY_CLASS = char[].class;
   private char defaultValue;

   public CharArrayMorpher()
   {
      super( false );
   }

   public CharArrayMorpher( char defaultValue )
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

      if( !(obj instanceof CharArrayMorpher) ){
         return false;
      }

      CharArrayMorpher other = (CharArrayMorpher) obj;
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

   public char getDefaultValue()
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

   public Object morph( Object array )
   {
      if( array == null ){
         return null;
      }

      if( CHAR_ARRAY_CLASS.isAssignableFrom( array.getClass() ) ){
         // no conversion needed
         return (char[]) array;
      }

      if( array.getClass()
            .isArray() ){
         int length = Array.getLength( array );
         int dims = getDimensions( array.getClass() );
         int[] dimensions = createDimensions( dims, length );
         Object result = Array.newInstance( char.class, dimensions );
         CharMorpher morpher = isUseDefault() ? new CharMorpher( defaultValue ) : new CharMorpher();
         if( dims == 1 ){
            for( int index = 0; index < length; index++ ){
               Array.set( result, index, new Character( morpher.morph( Array.get( array, index ) ) ) );
            }
         }else{
            for( int index = 0; index < length; index++ ){
               Array.set( result, index, morph( Array.get( array, index ) ) );
            }
         }
         return result;
      }else{
         throw new MorphException( "argument is not an array: " + array.getClass() );
      }
   }

   public Class morphsTo()
   {
      return CHAR_ARRAY_CLASS;
   }
}