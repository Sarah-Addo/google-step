// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;

public class Comment {

  /** List of descriptions of turns, e.g. "Player 1 took 3. New total: 18" */
  private final long id;
  private final String commentText;

  public Comment(long id, String commentText) {
    this.id = id;
    this.commentText = commentText;
  }
}