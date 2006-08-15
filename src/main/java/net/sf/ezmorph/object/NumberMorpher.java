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

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.primitive.ByteMorpher;
import net.sf.ezmorph.primitive.DoubleMorpher;
import net.sf.ezmorph.primitive.FloatMorpher;
import net.sf.ezmorph.primitive.IntMorpher;
import net.sf.ezmorph.primitive.LongMorpher;
import net.sf.ezmorph.primitive.ShortMorpher;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Morphs to a subclass of Number.<br>
 * Supported types are - Byte, Short, Integer, Long, Float, Number
 * 
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class NumberMorpher extends AbstractObjectMorpher
{
   private Number defaultValue;
   private Class type;

   public NumberMorpher( Class type )
   {
      super( false );

      if( type == null ){
         throw new MorphException( "Must specify a type" );
      }

      if( type != Byte.TYPE && type != Short.TYPE && type != Integer.TYPE && type != Long.TYPE
            && type != Float.TYPE && type != Double.TYPE && !Number.class.isAssignableFrom( type ) ){
         throw new MorphException( "Must specify a Number subclass" );
      }

      this.type = type;
   }

   public NumberMorpher( Class type, Number defaultValue )
   {
      super( true );

      if( type == null ){
         throw new MorphException( "Must specify a type" );
      }

      if( type != Byte.TYPE && type != Short.TYPE && type != Integer.TYPE && type != Long.TYPE
            && type != Float.TYPE && type != Double.TYPE && !Number.class.isAssignableFrom( type ) ){
         throw new MorphException( "Must specify a Number subclass" );
      }

      if( defaultValue != null && !type.isInstance( defaultValue ) ){
         throw new MorphException( "Default value must be of type " + type );
      }

      this.type = type;
      setDefaultValue( defaultValue );
   }

   public boolean equals( Object obj )
   {
      if( this == obj ){
         return true;
      }
      if( obj == null ){
         return false;
      }

      if( !(obj instanceof NumberMorpher) ){
         return false;
      }

      NumberMorpher other = (NumberMorpher) obj;
      EqualsBuilder builder = new EqualsBuilder();
      builder.append( type, other.type );
      if( isUseDefault() && other.isUseDefault() ){
         builder.append( getDefaultValue(), other.getDefaultValue() );
         return builder.isEquals();
      }else if( !isUseDefault() && !other.isUseDefault() ){
         return builder.isEquals();
      }else{
         return false;
      }
   }

   public Number getDefaultValue()
   {
      return defaultValue;
   }

   public int hashCode()
   {
      HashCodeBuilder builder = new HashCodeBuilder();
      builder.append( type );
      if( isUseDefault() ){
         builder.append( getDefaultValue() );
      }
      return builder.toHashCode();
   }

   public Object morph( Object value )
   {
      if( value != null && type.isAssignableFrom( value.getClass() ) ){
         // no conversion needed
         return value;
      }

      String str = String.valueOf( value )
            .trim();
      Object result = null;

      if( !type.isPrimitive() ){
         // if empty string and class != primitive treat it like null
         if( value == null || str.length() == 0 || "null".equalsIgnoreCase( str ) ){
            return null;
         }
      }

      if( isDecimalNumber( type ) ){
         if( Float.class.isAssignableFrom( type ) || Float.TYPE == type ){
            return morphToFloat( str );
         }else{
            return morphToDouble( str );
         }
      }else{
         if( Byte.class.isAssignableFrom( type ) || Byte.TYPE == type ){
            return morphToByte( str );
         }else if( Short.class.isAssignableFrom( type ) || Short.TYPE == type ){
            return morphToShort( str );
         }else if( Integer.class.isAssignableFrom( type ) || Integer.TYPE == type ){
            return morphToInteger( str );
         }else if( Long.class.isAssignableFrom( type ) || Long.TYPE == type ){
            return morphToLong( str );
         }
      }

      return result;
   }

   public Class morphsTo()
   {
      return type;
   }

   public void setDefaultValue( Number defaultValue )
   {
      this.defaultValue = defaultValue;
   }

   public boolean supports( Class clazz )
   {
      return !clazz.isArray();
   }

   private boolean isDecimalNumber( Class type )
   {
      return (Double.class.isAssignableFrom( type ) || Float.class.isAssignableFrom( type )
            || Double.TYPE == type || Float.TYPE == type);
   }

   private Object morphToByte( String str )
   {
      Object result = null;
      if( isUseDefault() ){
         if( defaultValue == null ){
            return (Byte) null;
         }else{
            result = new Byte( new ByteMorpher( defaultValue.byteValue() ).morph( str ) );
         }
      }else{
         result = new Byte( new ByteMorpher().morph( str ) );
      }
      return result;
   }

   private Object morphToDouble( String str )
   {
      Object result = null;
      if( isUseDefault() ){
         if( defaultValue == null ){
            return (Double) null;
         }else{
            result = new Double( new DoubleMorpher( defaultValue.doubleValue() ).morph( str ) );
         }
      }else{
         result = new Double( new DoubleMorpher().morph( str ) );
      }
      return result;
   }

   private Object morphToFloat( String str )
   {
      Object result = null;
      if( isUseDefault() ){
         if( defaultValue == null ){
            return (Float) null;
         }else{
            result = new Float( new FloatMorpher( defaultValue.floatValue() ).morph( str ) );
         }
      }else{
         result = new Float( new FloatMorpher().morph( str ) );
      }
      return result;
   }

   private Object morphToInteger( String str )
   {
      Object result = null;
      if( isUseDefault() ){
         if( defaultValue == null ){
            return (Integer) null;
         }else{
            result = new Integer( new IntMorpher( defaultValue.intValue() ).morph( str ) );
         }
      }else{
         result = new Integer( new IntMorpher().morph( str ) );
      }
      return result;
   }

   private Object morphToLong( String str )
   {
      Object result = null;
      if( isUseDefault() ){
         if( defaultValue == null ){
            return (Long) null;
         }else{
            result = new Long( new LongMorpher( defaultValue.longValue() ).morph( str ) );
         }
      }else{
         result = new Long( new LongMorpher().morph( str ) );
      }
      return result;
   }

   private Object morphToShort( String str )
   {
      Object result = null;
      if( isUseDefault() ){
         if( defaultValue == null ){
            return (Short) null;
         }else{
            result = new Short( new ShortMorpher( defaultValue.shortValue() ).morph( str ) );
         }
      }else{
         result = new Short( new ShortMorpher().morph( str ) );
      }
      return result;
   }
}