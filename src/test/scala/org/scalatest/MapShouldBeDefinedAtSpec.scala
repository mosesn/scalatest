/*
 * Copyright 2001-2013 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalatest

import SharedHelpers.thisLineNumber

class MapShouldBeDefinedAtSpec extends Spec with Matchers {
  
  object `PartialFunction ` {
    
    val map = Map(6 -> "six", 8 -> "eight")
    
    val map2 = Map(6 -> "enam", 8 -> "lapan")
    
    object `should be definedAt` {
      
      def `should do nothing when PartialFunction is defined at the specified value` {
        map should be definedAt (6)
      }
      
      def `should throw TestFailedException with correct stack depth when PartialFunction is not defined at the specified value` {
        val caught = intercept[TestFailedException] {
          map should be definedAt (0)
        }
        assert(caught.message === Some("0 was not defined at " + map))
        assert(caught.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught.failedCodeLineNumber === Some(thisLineNumber - 4))
      }
      
      def `should do nothing when both expressions in logical-and expression passed` {
        map should (be definedAt (6) and be definedAt (8))
        map should (be definedAt (6) and (be definedAt (8)))
        map should (be (definedAt (6)) and be (definedAt (8)))
        
        map should (equal (map) and be definedAt (8))
        map should (equal (map) and (be definedAt (8)))
        map should ((equal (map)) and be (definedAt (8)))
        
        map should (be definedAt (6) and equal (map))
        map should (be definedAt (6) and (equal (map)))
        map should (be (definedAt (6)) and (equal (map)))
      }
      
      def `should throw TestFailedException with correct stack depth when first expression in logical-and expression failed` {
        val caught1 = intercept[TestFailedException] {
          map should (be definedAt (0) and be definedAt (8))
        }
        assert(caught1.message === Some("0 was not defined at " + map))
        assert(caught1.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught1.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught2 = intercept[TestFailedException] {
          map should (be definedAt (0) and (be definedAt (8)))
        }
        assert(caught2.message === Some("0 was not defined at " + map))
        assert(caught2.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught2.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught3 = intercept[TestFailedException] {
          map should (be (definedAt (0)) and be (definedAt (8)))
        }
        assert(caught3.message === Some("0 was not defined at " + map))
        assert(caught3.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught3.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught4 = intercept[TestFailedException] {
          map should (equal (map2) and be definedAt (8))
        }
        assert(caught4.message === Some(map + " did not equal " + map2))
        assert(caught4.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught4.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught5 = intercept[TestFailedException] {
          map should (equal (map2) and (be definedAt (8)))
        }
        assert(caught5.message === Some(map + " did not equal " + map2))
        assert(caught5.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught5.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught6 = intercept[TestFailedException] {
          map should ((equal (map2)) and be (definedAt (8)))
        }
        assert(caught6.message === Some(map + " did not equal " + map2))
        assert(caught6.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught6.failedCodeLineNumber === Some(thisLineNumber - 4))
      }
      
      def `should throw TestFailedException with correct stack depth when second expression in logical-and expression failed` {
        val caught1 = intercept[TestFailedException] {
          map should (be definedAt (8) and be definedAt (0))
        }
        assert(caught1.message === Some("8 was defined at " + map + ", but 0 was not defined at " + map))
        assert(caught1.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught1.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught2 = intercept[TestFailedException] {
          map should (be definedAt (8) and (be definedAt (0)))
        }
        assert(caught2.message === Some("8 was defined at " + map + ", but 0 was not defined at " + map))
        assert(caught2.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught2.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught3 = intercept[TestFailedException] {
          map should (be (definedAt (8)) and be (definedAt (0)))
        }
        assert(caught3.message === Some("8 was defined at " + map + ", but 0 was not defined at " + map))
        assert(caught3.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught3.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught4 = intercept[TestFailedException] {
          map should (be definedAt (8) and equal (map2))
        }
        assert(caught4.message === Some("8 was defined at " + map + ", but " + map + " did not equal " + map2))
        assert(caught4.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught4.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught5 = intercept[TestFailedException] {
          map should (be definedAt (8) and (equal (map2)))
        }
        assert(caught5.message === Some("8 was defined at " + map + ", but " + map + " did not equal " + map2))
        assert(caught5.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught5.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught6 = intercept[TestFailedException] {
          map should (be (definedAt (8)) and (equal (map2)))
        }
        assert(caught6.message === Some("8 was defined at " + map + ", but " + map + " did not equal " + map2))
        assert(caught6.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught6.failedCodeLineNumber === Some(thisLineNumber - 4))
      }
      
      def `should throw TestFailedException with correct stack depth when both expression in logical-and expression failed` {
        val caught1 = intercept[TestFailedException] {
          map should (be definedAt (0) and be definedAt (0))
        }
        assert(caught1.message === Some("0 was not defined at " + map))
        assert(caught1.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught1.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught2 = intercept[TestFailedException] {
          map should (be definedAt (0) and (be definedAt (0)))
        }
        assert(caught2.message === Some("0 was not defined at " + map))
        assert(caught2.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught2.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught3 = intercept[TestFailedException] {
          map should (be (definedAt (0)) and be (definedAt (0)))
        }
        assert(caught3.message === Some("0 was not defined at " + map))
        assert(caught3.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught3.failedCodeLineNumber === Some(thisLineNumber - 4))
      }
      
      def `should do nothing when both expressions in logical-or expression passed` {
        map should (be definedAt (6) or be definedAt (8))
        map should (be definedAt (6) or (be definedAt (8)))
        map should (be (definedAt (6)) or be (definedAt (8)))
        
        map should (equal (map) or be definedAt (8))
        map should (equal (map) or (be definedAt (8)))
        map should ((equal (map)) or be (definedAt (8)))
        
        map should (be definedAt (6) or equal (map))
        map should (be definedAt (6) or (equal (map)))
        map should (be (definedAt (6)) or (equal (map)))
      }
      
      def `should do nothing when first expression in logical-or expression failed` {
        map should (be definedAt (0) or be definedAt (8))
        map should (be definedAt (0) or (be definedAt (8)))
        map should (be (definedAt (0)) or be (definedAt (8)))
        
        map should (equal (map2) or be definedAt (8))
        map should (equal (map2) or (be definedAt (8)))
        map should ((equal (map2)) or be (definedAt (8)))
        
        map should (be definedAt (0) or equal (map))
        map should (be definedAt (0) or (equal (map)))
        map should (be (definedAt (0)) or (equal (map)))
      }
      
      def `should do nothing when second expressions in logical-or expression failed` {
        map should (be definedAt (6) or be definedAt (0))
        map should (be definedAt (6) or (be definedAt (0)))
        map should (be (definedAt (6)) or be (definedAt (0)))
        
        map should (equal (map) or be definedAt (0))
        map should (equal (map) or (be definedAt (0)))
        map should ((equal (map)) or be (definedAt (0)))
        
        map should (be definedAt (6) or equal (map2))
        map should (be definedAt (6) or (equal (map2)))
        map should (be (definedAt (6)) or (equal (map2)))
      }
      
      def `should throw TestFailedException with correct stack depth when both expression in logical-or expression failed` {
        val caught1 = intercept[TestFailedException] {
          map should (be definedAt (0) or be definedAt (0))
        }
        assert(caught1.message === Some("0 was not defined at " + map + ", and 0 was not defined at " + map))
        assert(caught1.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught1.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught2 = intercept[TestFailedException] {
          map should (be definedAt (0) or (be definedAt (0)))
        }
        assert(caught2.message === Some("0 was not defined at " + map + ", and 0 was not defined at " + map))
        assert(caught2.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught2.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught3 = intercept[TestFailedException] {
          map should (be (definedAt (0)) or be (definedAt (0)))
        }
        assert(caught3.message === Some("0 was not defined at " + map + ", and 0 was not defined at " + map))
        assert(caught3.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught3.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught4 = intercept[TestFailedException] {
          map should (be definedAt (0) or equal (map2))
        }
        assert(caught4.message === Some("0 was not defined at " + map + ", and " + map + " did not equal " + map2))
        assert(caught4.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught4.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught5 = intercept[TestFailedException] {
          map should (equal (map2) or (be definedAt (0)))
        }
        assert(caught5.message === Some(map + " did not equal " + map2 + ", and 0 was not defined at " + map))
        assert(caught5.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught5.failedCodeLineNumber === Some(thisLineNumber - 4))
      }
    }
    
    object `should not be definedAt` {
      
      def `should do nothing when PartialFunction is not defined at the specified value` {
        map should not be definedAt (0)
      }
      
      def `should throw TestFailedException with correct stack depth when PartialFunction is defined at the specified value` {
        val caught = intercept[TestFailedException] {
          map should not be definedAt (8)
        }
        assert(caught.message === Some("8 was defined at " + map))
        assert(caught.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught.failedCodeLineNumber === Some(thisLineNumber - 4))
      }
      
      def `should do nothing when both expressions in logical-and expression passed` {
        map should (not be definedAt (0) and not be definedAt (0))
        map should (not be definedAt (0) and (not be definedAt (0)))
        map should (not be (definedAt (0)) and not be (definedAt (0)))
        
        map should (not equal (map2) and not be definedAt (0))
        map should (not equal (map2) and (not be definedAt (0)))
        map should ((not equal (map2)) and not be (definedAt (0)))
        
        map should (not be definedAt (0) and not equal (map2))
        map should (not be definedAt (0) and (not equal (map2)))
        map should (not be (definedAt (0)) and (not equal (map2)))
      }
      
      def `should throw TestFailedException with correct stack depth when first expression in logical-and expression failed` {
        val caught1 = intercept[TestFailedException] {
          map should (not be definedAt (8) and not be definedAt (0))
        }
        assert(caught1.message === Some("8 was defined at " + map))
        assert(caught1.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught1.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught2 = intercept[TestFailedException] {
          map should (not be definedAt (8) and (not be definedAt (0)))
        }
        assert(caught2.message === Some("8 was defined at " + map))
        assert(caught2.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught2.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught3 = intercept[TestFailedException] {
          map should (not be (definedAt (8)) and not be (definedAt (0)))
        }
        assert(caught3.message === Some("8 was defined at " + map))
        assert(caught3.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught3.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught4 = intercept[TestFailedException] {
          map should (not equal (map) and not be definedAt (0))
        }
        assert(caught4.message === Some(map + " equaled " + map))
        assert(caught4.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught4.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught5 = intercept[TestFailedException] {
          map should (not equal (map) and (not be definedAt (8)))
        }
        assert(caught5.message === Some(map + " equaled " + map))
        assert(caught5.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught5.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught6 = intercept[TestFailedException] {
          map should ((not equal (map)) and not be (definedAt (8)))
        }
        assert(caught6.message === Some(map + " equaled " + map))
        assert(caught6.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught6.failedCodeLineNumber === Some(thisLineNumber - 4))
      }
      
      def `should throw TestFailedException with correct stack depth when second expression in logical-and expression failed` {
        val caught1 = intercept[TestFailedException] {
          map should (not be definedAt (0) and not be definedAt (8))
        }
        assert(caught1.message === Some("0 was not defined at " + map + ", but 8 was defined at " + map))
        assert(caught1.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught1.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught2 = intercept[TestFailedException] {
          map should (not be definedAt (0) and (not be definedAt (8)))
        }
        assert(caught2.message === Some("0 was not defined at " + map + ", but 8 was defined at " + map))
        assert(caught2.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught2.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught3 = intercept[TestFailedException] {
          map should (not be (definedAt (0)) and not be (definedAt (8)))
        }
        assert(caught3.message === Some("0 was not defined at " + map + ", but 8 was defined at " + map))
        assert(caught3.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught3.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught4 = intercept[TestFailedException] {
          map should (not be definedAt (0) and not equal (map))
        }
        assert(caught4.message === Some("0 was not defined at " + map + ", but " + map + " equaled " + map))
        assert(caught4.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught4.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught5 = intercept[TestFailedException] {
          map should (not be definedAt (0) and (not equal (map)))
        }
        assert(caught5.message === Some("0 was not defined at " + map + ", but " + map + " equaled " + map))
        assert(caught5.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught5.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught6 = intercept[TestFailedException] {
          map should (not be (definedAt (0)) and (not equal (map)))
        }
        assert(caught6.message === Some("0 was not defined at " + map + ", but " + map + " equaled " + map))
        assert(caught6.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught6.failedCodeLineNumber === Some(thisLineNumber - 4))
      }
      
      def `should throw TestFailedException with correct stack depth when both expression in logical-and expression failed` {
        val caught1 = intercept[TestFailedException] {
          map should (not be definedAt (8) and not be definedAt (8))
        }
        assert(caught1.message === Some("8 was defined at " + map))
        assert(caught1.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught1.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught2 = intercept[TestFailedException] {
          map should (not be definedAt (8) and (not be definedAt (8)))
        }
        assert(caught2.message === Some("8 was defined at " + map))
        assert(caught2.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught2.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught3 = intercept[TestFailedException] {
          map should (not be (definedAt (8)) and not be (definedAt (8)))
        }
        assert(caught3.message === Some("8 was defined at " + map))
        assert(caught3.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught3.failedCodeLineNumber === Some(thisLineNumber - 4))
      }
      
      def `should do nothing when both expressions in logical-or expression passed` {
        map should (not be definedAt (0) or not be definedAt (0))
        map should (not be definedAt (0) or (not be definedAt (0)))
        map should (not be (definedAt (0)) or not be (definedAt (0)))
        
        map should (not equal (map2) or not be definedAt (0))
        map should (not equal (map2) or (not be definedAt (0)))
        map should ((not equal (map2)) or not be (definedAt (0)))
        
        map should (not be definedAt (0) or not equal (map2))
        map should (not be definedAt (0) or (not equal (map2)))
        map should (not be (definedAt (0)) or (not equal (map2)))
      }
      
      def `should do nothing when first expression in logical-or expression failed` {
        map should (not be definedAt (8) or not be definedAt (0))
        map should (not be definedAt (8) or (not be definedAt (0)))
        map should (not be (definedAt (8)) or not be (definedAt (0)))
        
        map should (not equal (map) or not be definedAt (0))
        map should (not equal (map) or (not be definedAt (0)))
        map should ((not equal (map)) or not be (definedAt (0)))
        
        map should (not be definedAt (8) or not equal (map2))
        map should (not be definedAt (8) or (not equal (map2)))
        map should (not be (definedAt (8)) or (not equal (map2)))
      }
      
      def `should do nothing when second expressions in logical-or expression failed` {
        map should (not be definedAt (0) or not be definedAt (8))
        map should (not be definedAt (0) or (not be definedAt (8)))
        map should (not be (definedAt (0)) or not be (definedAt (8)))
        
        map should (not equal (map2) or not be definedAt (8))
        map should (not equal (map2) or (not be definedAt (8)))
        map should ((not equal (map2)) or not be (definedAt (8)))
        
        map should (not be definedAt (0) or not equal (map))
        map should (not be definedAt (0) or (not equal (map)))
        map should (not be (definedAt (0)) or (not equal (map)))
      }
      
      def `should throw TestFailedException with correct stack depth when both expression in logical-or expression failed` {
        val caught1 = intercept[TestFailedException] {
          map should (not be definedAt (8) or not be definedAt (8))
        }
        assert(caught1.message === Some("8 was defined at " + map + ", and 8 was defined at " + map))
        assert(caught1.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught1.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught2 = intercept[TestFailedException] {
          map should (not be definedAt (8) or (not be definedAt (8)))
        }
        assert(caught2.message === Some("8 was defined at " + map + ", and 8 was defined at " + map))
        assert(caught2.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught2.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught3 = intercept[TestFailedException] {
          map should (not be (definedAt (8)) or not be (definedAt (8)))
        }
        assert(caught3.message === Some("8 was defined at " + map + ", and 8 was defined at " + map))
        assert(caught3.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught3.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught4 = intercept[TestFailedException] {
          map should (not be definedAt (8) or not equal (map))
        }
        assert(caught4.message === Some("8 was defined at " + map + ", and " + map + " equaled " + map))
        assert(caught4.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught4.failedCodeLineNumber === Some(thisLineNumber - 4))
        
        val caught5 = intercept[TestFailedException] {
          map should (not equal (map) or (not be definedAt (8)))
        }
        assert(caught5.message === Some(map + " equaled " + map + ", and 8 was defined at " + map))
        assert(caught5.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught5.failedCodeLineNumber === Some(thisLineNumber - 4))
      }
      
    }
    
    object `shouldNot be definedAt` {
      
      def `should do nothing when PartialFunction is not defined at the specified value` {
        map shouldNot be definedAt (0)
      }
      
      def `should throw TestFailedException with correct stack depth when PartialFunction is defined at the specified value` {
        val caught = intercept[TestFailedException] {
          map shouldNot be definedAt (8)
        }
        assert(caught.message === Some("8 was defined at " + map))
        assert(caught.failedCodeFileName === Some("MapShouldBeDefinedAtSpec.scala"))
        assert(caught.failedCodeLineNumber === Some(thisLineNumber - 4))
      }
    }
  }
  
}