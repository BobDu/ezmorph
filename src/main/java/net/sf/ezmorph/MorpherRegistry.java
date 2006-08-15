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

package net.sf.ezmorph;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.IdentityObjectMorpher;

/**
 * Convenient class that manages Morphers.<br>
 * 
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class MorpherRegistry
{
   private Map morphers = new HashMap();

   public MorpherRegistry()
   {

   }

   /**
    * Deregisters all morphers.
    */
   public void clear()
   {
      morphers.clear();
   }

   /**
    * Deregister the specified Morpher.<br>
    * The registry will remove the target <code>Class</code> from the morphers
    * Map if it has no other registered morphers.
    * 
    * @param morpher
    */
   public void deregisterMorpher( Morpher morpher )
   {
      List registered = (List) morphers.get( morpher.morphsTo() );
      if( registered != null && !registered.isEmpty() ){
         registered.remove( morpher );
         if( registered.isEmpty() ){
            morphers.remove( morpher.morphsTo() );
         }
      }
   }

   /**
    * Returns a morpher for <code>clazz</code>.<br>
    * If several morphers are found for that class, it returns the first. If no
    * Morpher is found it will return the IdentityObjectMorpher.
    */
   public Morpher getMorpherFor( Class clazz )
   {
      List registered = (List) morphers.get( clazz );
      if( registered == null || registered.isEmpty() ){
         // no morpher registered for clazz
         return IdentityObjectMorpher.getInstance();
      }else{
         return (Morpher) registered.get( 0 );
      }
   }

   /**
    * Returns all morphers for <code>clazz</code>.<br>
    * If no Morphers are found it will return an array containing the
    * IdentityObjectMorpher.
    */
   public Morpher[] getMorphersFor( Class clazz )
   {
      List registered = (List) morphers.get( clazz );
      if( registered == null || registered.isEmpty() ){
         // no morphers registered for clazz
         return new Morpher[] { IdentityObjectMorpher.getInstance() };
      }else{
         Morpher[] morphs = new Morpher[registered.size()];
         int k = 0;
         for( Iterator i = registered.iterator(); i.hasNext(); ){
            morphs[k++] = (Morpher) i.next();
         }
         return morphs;
      }
   }

   /**
    * Morphs and object to the specified target class.<br>
    * This method uses reflection to invoke primitive Morphers and Morphers that
    * do not implement ObjectMorpher.
    * 
    * @param target
    * @param value
    * @return
    */
   public Object morph( Class target, Object value )
   {
      Morpher morpher = getMorpherFor( target );
      if( morpher instanceof ObjectMorpher ){
         return ((ObjectMorpher) morpher).morph( value );
      }else{
         try{
            Method morphMethod = morpher.getClass()
                  .getDeclaredMethod( "morph", new Class[] { Object.class } );
            return morphMethod.invoke( morpher, new Object[] { value } );
         }
         catch( Exception e ){
            throw new MorphException( e );
         }
      }
   }

   /**
    * Register a Morpher for a target <code>Class</code>.<br>
    * The target class is the class this Morpher morphs to. If there are another
    * morphers registered to that class, it will be appended to a List.
    * 
    * @param morpher
    */
   public void registerMorpher( Morpher morpher )
   {
      List registered = (List) morphers.get( morpher.morphsTo() );
      if( registered == null ){
         registered = new ArrayList();
         morphers.put( morpher.morphsTo(), registered );
      }
      if( !registered.contains( morpher ) ){
         registered.add( morpher );
      }
   }
}