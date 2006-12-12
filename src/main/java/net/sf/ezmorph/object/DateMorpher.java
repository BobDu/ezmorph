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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.ezmorph.MorphException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Morphs a String to a Date.<br>
 * <p>
 * This morpher will iterate through the supplied formats until one succeeds or
 * the default value is returned (if default value is condigured).
 * </p>
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class DateMorpher extends AbstractObjectMorpher
{
   private Date defaultValue;
   private String[] formats;
   private boolean lenient;
   private Locale locale;

   public DateMorpher( String[] formats )
   {
      this( formats, Locale.getDefault(), false );
   }

   public DateMorpher( String[] formats, Date defaultValue )
   {
      this( formats, defaultValue, Locale.getDefault(), false );
   }

   public DateMorpher( String[] formats, Date defaultValue, Locale locale, boolean lenient )
   {
      super( true );
      if( formats == null || formats.length == 0 ){
         throw new IllegalArgumentException( "invalid array of formats" );
      }
      // should use defensive copying ?
      this.formats = formats;

      if( locale == null ){
         this.locale = Locale.getDefault();
      }else{
         this.locale = locale;
      }

      this.lenient = lenient;
      setDefaultValue( defaultValue );
   }

   public DateMorpher( String[] formats, Locale locale, boolean lenient )
   {
      if( formats == null || formats.length == 0 ){
         throw new IllegalArgumentException( "invalid array of formats" );
      }
      // should use defensive copying ?
      this.formats = formats;

      if( locale == null ){
         this.locale = Locale.getDefault();
      }else{
         this.locale = locale;
      }

      this.lenient = lenient;
   }

   public boolean equals( Object obj )
   {
      if( this == obj ){
         return true;
      }
      if( obj == null ){
         return false;
      }

      if( !(obj instanceof DateMorpher) ){
         return false;
      }

      DateMorpher other = (DateMorpher) obj;
      EqualsBuilder builder = new EqualsBuilder();
      builder.append( formats, other.formats );
      builder.append( locale, other.locale );
      builder.append( lenient, other.lenient );
      if( isUseDefault() && other.isUseDefault() ){
         builder.append( getDefaultValue(), other.getDefaultValue() );
         return builder.isEquals();
      }else if( !isUseDefault() && !other.isUseDefault() ){
         return builder.isEquals();
      }else{
         return false;
      }
   }

   public Date getDefaultValue()
   {
      return (Date) defaultValue.clone();
   }

   public int hashCode()
   {
      HashCodeBuilder builder = new HashCodeBuilder();
      builder.append( formats );
      builder.append( locale );
      builder.append( lenient );
      if( isUseDefault() ){
         builder.append( getDefaultValue() );
      }
      return builder.toHashCode();
   }

   public Object morph( Object value )
   {
      if( value == null ){
         return null;
      }

      if( Date.class.isAssignableFrom( value.getClass() ) ){
         return (Date) value;
      }

      if( !supports( value.getClass() ) ){
         throw new MorphException( value.getClass() + " is not supported" );
      }

      String strValue = (String) value;
      SimpleDateFormat dateParser = null;

      for( int i = 0; i < formats.length; i++ ){
         if( dateParser == null ){
            dateParser = new SimpleDateFormat( formats[i], locale );
         }else{
            dateParser.applyPattern( formats[i] );
         }
         dateParser.setLenient( lenient );
         try{
            return dateParser.parse( strValue.toLowerCase() );
         }
         catch( ParseException pe ){
            // ignore exception, try the next format
         }
      }

      // unable to parse the date
      if( isUseDefault() ){
         return defaultValue;
      }else{
         throw new MorphException( "Unable to parse the date " + value );
      }
   }

   public Class morphsTo()
   {
      return Date.class;
   }

   public void setDefaultValue( Date defaultValue )
   {
      this.defaultValue = (Date) defaultValue.clone();
   }

   public boolean supports( Class clazz )
   {
      return String.class.isAssignableFrom( clazz );
   }
}