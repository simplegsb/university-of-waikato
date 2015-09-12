This is a list of the models in this directory,
together with the number of states constrcuted by VALID
when checking controllability, and the expected result.

Note that some of the central locking and transfer line
examples are very large and probably cannot be solved by
standard state exploration.

Directory and model  #States Controllable  Notes
=============================================================================
big_factory/                               Big factory extended with breadown
  bfactory/               40   no
bmw_fh/                                    BMW window lift controller
  bmw_fh                7672   yes
central_locking/                           BMW central locking system
  dreitueren          420283   yes
  ftuer                  195   yes
  koordwsp            465648   yes
  tuer1                  226   yes
  tuer2                  238   yes
  verriegel              ???   yes
  vtueren               8407   yes
debounce/
  debounce                 6   yes
falko/
  falko                 1536   yes
fischertechnik/
  fischertechnik          21   no
safety_display/
  safetydisplay           81   yes
  safetydisplay_uncont    28   no
small_factory/                             Plain old small factory example
  sfactory                12   yes
  sfactory_uncont          8   no
smd/
  smdreset                31   yes
tictactoe/                                 Tic-tac-toe game without controller
  tictactoe             2422   no
tline_1/                                   Transfer line
  tline_1                 28   yes
tline_2/                                   Repeated transfer lines ...
  tline_2                410   yes
tline_3/
  tline_3               5992   yes
tline_4/
  tline_4              87578   yes
tline_5/
  tline_5            1280020   yes
tline_6/
  tline_6           18749318   yes
tline_7/
  tline_7          274115022   yes
vt/
  weiche               23104   yes
