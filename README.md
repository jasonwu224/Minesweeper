# Minesweeper

My final project for AP Computer Science. Thank you Mrs. Honerkamp for an amazing year!

## How to Play

Cells can either be hidden, revealed, or flagged. Revealed cells can be empty, contain a number, or contain a mine.

All cells on the grid are hidden in the beginning. Reveal any to begin, don't worry you can't lose on the first one! 

When cells are revealed, they show a number which indicates the number of surrounding mines (in the 8 cells around the revealed cell).
Tiles without any surrounding mines simply have no number. 

Deduce which cells contain mines using logic and the numbbers revealed. Place a flag on any cell suspected to contain a mine.
Once all safe cells are revealed, the game is won. 

## Controls

Left click to open cells, right click to flag. Left click on a numbered cell to reveal surrounding cells, this only works
if the amount of surrounding flagged cells is equal to the number (e.g. there must be three flags surrounding a 3-cell 
for the remaining surrounding hidden cells to be revealed). 
