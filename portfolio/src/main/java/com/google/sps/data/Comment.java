// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

public class Comment {

  private final long id;
  private final String commentText;
  private final long timestamp;

  public Comment(long id, String commentText, long timestamp) {
    this.id = id;
    this.commentText = commentText;
    this.timestamp = timestamp;
  }
}
