/*
  * Copyright (c) , , Oracle and/or its affiliates. All rights reserved.
  * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
  *
  * This code is free software; you can redistribute it and/or modify it
  * under the terms of the GNU General Public License version  only, as
  * published by the Free Software Foundation.  Oracle designates this
  * particular file as subject to the "Classpath" exception as provided
  * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version  for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 *  along with this work; if not, write to the Free Software Foundation,
 * Inc.,  Franklin St, Fifth Floor, Boston, MA - USA.
 *
 * Please contact Oracle,  Oracle Parkway, Redwood Shores, CA  USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/** A generic class for pairs.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
 public class Pair<T1, T2> {
     public final T1 fst;
     public final T2 snd;

     public Pair(T1 fst, T2 snd){
         this.fst = fst;
         this.snd = snd;
     }

    private static boolean equals(Object x, Object y) {
       return (x == null && y == null) || (x != null && x.equals(y));
    }
    public boolean equals(Object other) {
       return other instanceof Pair<?,?> && equals(fst, ((Pair<?,?>)other).fst) && equals(snd, ((Pair<?,?>)other).snd);
     }

    public int hashCode() {
       if (fst == null) return (snd == null) ? 0 : snd.hashCode() + 1;
       else if (snd == null) return fst.hashCode() + 2;
       else return fst.hashCode() * 17 + snd.hashCode();
    }

     public static <T1,T2> Pair<T1,T2> of(T1 fst, T2 snd) {
         return new Pair<T1,T2>(fst,snd);
      }
 }
