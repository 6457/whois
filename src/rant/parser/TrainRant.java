package rant.parser;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import pipe.TokenLineLexical;
import pipe.TokenLineStructure;
import rant.tagger.RantTagger;
import sq.data.Instance;
import sq.data.InstanceList;
import sq.data.LabelSequence;
import sq.data.TokenSequence;
import sq.model.CRF;
import sq.trainer.LBFGSPTrainer;
import sq.trainer.LBFGSTrainer;
import sq.trainer.Options;
import sq.trainer.SGDTrainer;
import sq.trainer.Trainer;
import sq.utils.Timer;
import thick.ThickFactory;

public class TrainRant {
	public static void main(String[] args) {
		Options opt = new Options();
		if (opt.parseCommand(args) != null) {
			System.out.println(opt);
		} else {
			return;
		}
		trainModel(readData(), opt, null);
	}

	public static void trainModel(Collection<String> data, Options opt, String modelFile) {
		try {
			InstanceList list = genList(data);
			CRF crf = new CRF(list);
			System.out.println("\nSize of train: \t" + list.size());
			System.out.println("Number of states: \t" + list.getStateNumber());
			System.out.println("Number of unary features: \t" + list.getUnarySize());
			System.out.println("Number of binary features: \t" + list.getBinarySize());
			System.out.println("Number of parameters: \t" + crf.size());

			Trainer trainer;
			switch (opt.getTrainer()) {
			case 0:
				trainer = new SGDTrainer(crf);
				break;
			case 1:
				trainer = new LBFGSTrainer(crf);
				break;
			case 2:
				trainer = new LBFGSPTrainer(crf);
				break;
			default:
				System.err.println(opt.help());
				return;
			}

			System.out.println("Training model using: " + trainer.toString());

			Timer.on();

			trainer.train(opt);
			if (modelFile != null) {
				crf.write(new File(modelFile));
			}
			System.out.println("====================");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static InstanceList genList(Collection<String> data) throws IOException {
		RantTagger tagger = new RantTagger();

		InstanceList list = new InstanceList(tagger.getAlphabet());
		list.addPipe(new TokenLineStructure());
		list.addPipe(new TokenLineLexical());

		for (String whois : data) {
			TokenSequence docum = ThickFactory.chunk(whois);
			LabelSequence ls = tagger.tag(docum);
			Instance inst = new Instance(docum, ls);
			list.add(inst);
		}
		list.dump(3, 100);
		return list;
	}
	
	// Plug in your favorite way of reading data
	public static Collection<String> readData() {
		return null;
	}
}