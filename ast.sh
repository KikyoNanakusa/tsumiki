mvn -q -e exec:java -Dexec.mainClass="net.nanakusa.compiler.Compiler" -Dexec.args="code.txt --ast" >output.s
cat output.s

DIRECTORY="ast_dot"
OUTDIR="ast_png"
DPI=300

mkdir -p "$OUTDIR"
rm -f "$OUTDIR"/*.png

for file in "$DIRECTORY"/*.dot; do
  base_name=$(basename "$file" .dot)

  dot -Tpng "$file" -o "$OUTDIR/$base_name.png" \
    -Gfontname="Cute Font" -Nfontname="Cute Font" -Efontname="Cute Font" \
    -Gdpi="$DPI"
done
