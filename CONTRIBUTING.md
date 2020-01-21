# Check gradle
Ensure you use the gradle wrapper / ensure you are using gradle v6.0.1

(See `gradle/wrapper/gradle-wrapper.properties`)

# Add git pre-commit hook
Run the `addKtlintFormatGitPreCommitHook` gradle task

Ensure `pre-commit` exists in `.git/hooks`

If it doesn't, create it with this code:
```bash
#!/bin/sh
set -e
######## KTLINT-GRADLE HOOK START ########

CHANGED_FILES="$(git --no-pager diff --name-status --no-color --cached | awk '$1 != "D" && $2 ~ /\.kts|\.kt/ { print $2}')"

if [ -z "$CHANGED_FILES" ]; then
    echo "No Kotlin staged files."
    exit 0
fi;

echo "Running ktlint over these files:"
echo "$CHANGED_FILES"

./gradlew --quiet ktlintFormat -PinternalKtlintGitFilter="$CHANGED_FILES"

echo "Completed ktlint run."

echo "$CHANGED_FILES" | while read -r file; do
    if [ -f $file ]; then
        git add $file
    fi
done

echo "Completed ktlint hook."
######## KTLINT-GRADLE HOOK END ########
```
