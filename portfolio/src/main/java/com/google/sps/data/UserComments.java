// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;

public class UserComments {

  /** List of descriptions of turns, e.g. "Player 1 took 3. New total: 18" */
  private final ArrayList<String> comments = new ArrayList<>();

  public void addComment (String comment) {
      comments.add(comment);
  }
}